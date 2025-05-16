package net.alteiar.lendyr.game.encounter;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.client.grpc.v1.GameService;
import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.model.AttackResult;
import net.alteiar.lendyr.entity.notification.NotificationMessage;
import net.alteiar.lendyr.entity.notification.NotificationMessageFactory;
import net.alteiar.lendyr.entity.notification.NotificationType;
import net.alteiar.lendyr.game.encounter.controller.EncounterController;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrActionResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Log4j2
public class GameEngine {

  private final ExecutorService executorService;

  @Getter
  private final EncounterController encounterController;

  private final GameService gameService;

  @Getter
  private final EncounterEntity encounterEntity;

  @Getter
  private boolean gameOver;
  @Getter
  private boolean gameWon;

  @Getter
  private boolean networkError;
  @Getter
  private String networkErrorMessage;

  @Builder
  GameEngine(@NonNull String host, int port) {
    this.gameService = GameService.builder().host(host).port(port).build();
    this.encounterEntity = EncounterEntity.builder().build();
    this.gameOver = false;
    this.gameWon = false;

    this.executorService = Executors.newSingleThreadExecutor();
    this.encounterController = EncounterController.builder().build();
  }

  public void load() {
    this.gameService.start();

    executorService.submit(() -> {
      log.info("Loading game...");
      try {
        log.info("Loading initial state...");
        boolean loaded = this.gameService.getGameClient().forceLoad(this.encounterEntity);

        if (!loaded) {
          networkError = true;
          networkErrorMessage = "Failed to load game! Server might be down.";
          return;
        }
        log.info("Initial state loaded");
      } catch (RuntimeException e) {
        log.error("Unexpected error while loading the initial state", e);
        networkError = true;
        networkErrorMessage = "Failed to load game! Server might be down.";
        return;
      }
      try {
        log.info("Registering for game change...");
        this.gameService.getGameClient().register(this.encounterEntity);
        log.info("Registered");
      } catch (RuntimeException e) {
        log.error("Failed to register for game change.", e);
        networkError = true;
        networkErrorMessage = "Failed register to server! Server might be down.";
        return;
      }
      log.info("Game is loaded");
    });
  }

  public void dispose() {
    log.info("Dispose the game...");
    this.gameService.stop();
    this.executorService.shutdown();
  }

  public boolean isLoaded() {
    return this.encounterEntity.isLoaded();
  }

  public void move(PersonaEntity character, Vector2 newPosition) {
    if (!Objects.equals(encounterEntity.getCurrentCharacter(), character)) {
      throw new IllegalStateException("Character move not allowed; it's not your turn");
    }
    if (newPosition.dst2(character.getPosition()) > Math.pow(character.getSpeed(), 2)) {
      throw new IllegalStateException("Character can't move this far");
    }

    executorService.submit(() -> {
      try {
        LendyrActionResult result = gameService.getGameClient().move(character.getId(), List.of(newPosition));

        switch (result.getType()) {
          case NOT_ENOUGH_ACTION -> {
            encounterController.pushNotification(NotificationMessageFactory.warning("Minor action is already used"));
          }
          case NOT_ALLOWED -> {
            encounterController.pushNotification(NotificationMessageFactory.warning("Not allowed: " + result.getError()));
          }
          case NOT_YOUR_TURN -> {
            encounterController.pushNotification(NotificationMessageFactory.warning("You can't play, it's not your turn"));
          }
          case NOT_FOUND -> {
            encounterController.pushNotification(NotificationMessageFactory.warning("Count not find the character to move"));
          }
          case UNKNOWN, NOT_IMPLEMENTED, UNEXPECTED, UNRECOGNIZED -> {
            encounterController.pushNotification(NotificationMessageFactory.error(result.getError().getDescription()));
          }
          case SUCCESS -> log.info("Move is successful");
        }
      } catch (RuntimeException e) {
        encounterController.pushNotification(NotificationMessage.builder().notificationType(NotificationType.ERROR).message(e.getMessage()).build());
      }
    });
  }

  public void submitAttack(PersonaEntity from, PersonaEntity to) {
    executorService.submit(() -> {
      try {
        AttackResult result = gameService.getGameClient().attack(from.getId(), to.getId());
        encounterController.pushDamage(result);
      } catch (RuntimeException e) {
        log.warn("Error", e);
        encounterController.pushNotification(NotificationMessageFactory.warning(e.getMessage()));
      }
    });
  }

  public void endCharacterTurn() {
    executorService.submit(() -> gameService.getGameClient().endPlayerTurn());
  }
}
