package net.alteiar.lendyr.client.grpc.v1;

import com.badlogic.gdx.math.Vector2;
import io.grpc.Channel;
import io.grpc.StatusException;
import io.grpc.stub.BlockingClientCall;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.entity.mapper.AttackMapper;
import net.alteiar.lendyr.entity.mapper.GenericMapper;
import net.alteiar.lendyr.entity.model.AttackResult;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrActionResult;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrAttackAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrMoveAction;
import net.alteiar.lendyr.grpc.model.v1.game.EmptyResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class GameClient {
  private static final int MAX_RETRY = 5;
  private static final long BACKOFF = 100;
  private static final float BACKOFF_FACTOR = 1.2f;

  private final Thread currentStateProcessorThread;
  private final CurrentStateProcessor currentStateProcessor;

  private final LendyrGameServiceGrpc.LendyrGameServiceBlockingV2Stub blockingStub;

  GameClient(Channel channel) {
    currentStateProcessor = new CurrentStateProcessor();
    currentStateProcessorThread = Thread.ofPlatform().name("current-state-stream-processor").unstarted(currentStateProcessor);

    blockingStub = LendyrGameServiceGrpc.newBlockingV2Stub(channel);
  }

  public void terminate() {
    currentStateProcessor.terminate();
    currentStateProcessorThread.interrupt();
  }

  /**
   * Force load the game
   *
   * @param encounterEntity the encounter entity to feed.
   * @return true if the game loaded
   */
  public boolean forceLoad(EncounterEntity encounterEntity) {
    boolean retrieved = false;
    int retry = 0;
    long backoff = BACKOFF;
    while (!retrieved && retry < MAX_RETRY) {
      try {
        LendyrGameState gameState = blockingStub.withDeadlineAfter(Duration.ofSeconds(1)).currentState(EmptyResponse.newBuilder().build());
        encounterEntity.newState(gameState);
        retrieved = true;
      } catch (RuntimeException e) {
        retry++;
        try {
          Thread.sleep(backoff);
        } catch (InterruptedException ie) {
          log.info("Interrupted during backoff time.", ie);
          return false;
        }
        backoff = (long) (backoff * BACKOFF_FACTOR);
        log.warn("Failed to load game state, retry {}/{}", retry, MAX_RETRY);
      }
    }

    return retrieved;
  }

  public void register(EncounterEntity encounterEntity) {
    currentStateProcessor.setEncounterEntity(encounterEntity);
    currentStateProcessorThread.start();
  }

  public void endPlayerTurn() {
    this.blockingStub.withDeadlineAfter(Duration.ofMillis(300)).endTurn(EmptyResponse.getDefaultInstance());
  }

  public LendyrActionResult move(UUID sourceId, List<Vector2> positions) {
    LendyrMoveAction moveAction = LendyrMoveAction.newBuilder()
      .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(sourceId))
      .addAllPosition(positions.stream().map(GenericMapper.INSTANCE::convertPosition).toList())
      .build();
    LendyrAction request = LendyrAction.newBuilder().setMove(moveAction).build();

    return act(request);
  }

  public AttackResult attack(UUID sourceId, UUID targetId) {
    LendyrAttackAction attack = LendyrAttackAction.newBuilder()
      .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(sourceId))
      .setTargetId(GenericMapper.INSTANCE.convertUUIDToBytes(targetId))
      .build();
    LendyrAction request = LendyrAction.newBuilder().setAttack(attack).build();

    LendyrActionResult result = act(request);
    if (result.hasError()) {
      throw new RuntimeException(result.getError().getDescription());
    }
    return AttackMapper.INSTANCE.convertToBusiness(sourceId, targetId, result.getAttack());
  }

  private LendyrActionResult act(LendyrAction action) {
    return blockingStub.withDeadlineAfter(Duration.ofMillis(300)).act(action);
  }

  private class CurrentStateProcessor implements Runnable {
    private final AtomicBoolean terminate = new AtomicBoolean(false);

    @Setter
    private EncounterEntity encounterEntity;

    public void terminate() {
      terminate.set(true);
    }

    @Override
    public void run() {
      BlockingClientCall<?, LendyrGameState> clientCall = blockingStub.registerCurrentState(EmptyResponse.newBuilder().build());
      while (!terminate.get()) {
        try {
          LendyrGameState lendyrGameState = clientCall.read();
          encounterEntity.newState(lendyrGameState);
        } catch (InterruptedException e) {
          log.debug("Interrupted", e);
        } catch (StatusException e) {
          log.warn("Failed to retrieve game state", e);
        }
      }
      clientCall.cancel("Client terminate", null);
      log.warn("Current state processor terminated");
    }
  }
}
