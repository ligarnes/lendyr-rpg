package net.alteiar.lendyr.client.grpc.v1;

import com.badlogic.gdx.math.Vector2;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import lombok.Builder;
import net.alteiar.lendyr.entity.CombatEntity;
import net.alteiar.lendyr.entity.mapper.GenericMapper;
import net.alteiar.lendyr.grpc.model.v1.EmptyResponse;
import net.alteiar.lendyr.grpc.model.v1.LendyrGameServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.LendyrGameState;
import net.alteiar.lendyr.grpc.model.v1.combat.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class GameClient {

  private final LendyrGameServiceGrpc.LendyrGameServiceStub stub;
  private final LendyrGameServiceGrpc.LendyrGameServiceBlockingStub blockingStub;

  GameClient(Channel channel) {
    stub = LendyrGameServiceGrpc.newStub(channel);
    blockingStub = LendyrGameServiceGrpc.newBlockingStub(channel);
  }

  public void forceLoad(CombatEntity combatEntity) {
    boolean retrieved = false;
    while (!retrieved) {
      try {
        LendyrGameState gameState = blockingStub.withDeadlineAfter(Duration.ofMillis(100)).currentState(EmptyResponse.newBuilder().build());
        combatEntity.newState(gameState);
        retrieved = true;
      } catch (RuntimeException e) {
        System.out.println("Failed to load game state: " + e.getMessage());
      }
    }
  }

  public void register(CombatEntity combatEntity) {
    stub.registerCurrentState(EmptyResponse.newBuilder().build(), GameStateObserver.builder().combatEntity(combatEntity).build());
  }

  public void endPlayerTurn() {
    this.blockingStub.withDeadlineAfter(Duration.ofMillis(100)).endTurn(EmptyResponse.getDefaultInstance());
  }

  public LendyrActionResult move(UUID sourceId, List<Vector2> positions) {
    LendyrMoveAction moveAction = LendyrMoveAction.newBuilder()
      .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(sourceId))
      .addAllPosition(positions.stream().map(GenericMapper.INSTANCE::convertPosition).toList())
      .build();
    LendyrAction request = LendyrAction.newBuilder().setMove(moveAction).build();

    return act(request);
  }

  public LendyrAttackActionResult attack(UUID sourceId, UUID targetId) {
    LendyrAttackAction attack = LendyrAttackAction.newBuilder()
      .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(sourceId))
      .setTargetId(GenericMapper.INSTANCE.convertUUIDToBytes(targetId))
      .build();
    LendyrAction request = LendyrAction.newBuilder().setAttack(attack).build();

    LendyrActionResult result = act(request);
    if (result.hasError()) {
      throw new RuntimeException(result.getError().getDescription());
    }
    return result.getAttack();
  }

  private LendyrActionResult act(LendyrAction action) {
    return blockingStub.withDeadlineAfter(Duration.ofMillis(100)).act(action);
  }

  @Builder
  private static class GameStateObserver implements StreamObserver<LendyrGameState> {
    private final CombatEntity combatEntity;

    @Override
    public void onNext(LendyrGameState lendyrCombatState) {
      try {
        combatEntity.newState(lendyrCombatState);
      } catch (RuntimeException e) {
        System.out.println("Unexpected error: " + e.getMessage());
        e.printStackTrace();
      }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
    }
  }
}
