package net.alteiar.lendyr.game.state;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.client.grpc.v1.GameService;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.entity.CombatEntity;
import net.alteiar.lendyr.entity.notification.NotificationMessage;
import net.alteiar.lendyr.entity.notification.NotificationMessageFactory;
import net.alteiar.lendyr.entity.notification.NotificationType;
import net.alteiar.lendyr.game.screen.NotificationManager;
import net.alteiar.lendyr.grpc.model.v1.combat.LendyrActionResult;
import net.alteiar.lendyr.grpc.model.v1.combat.LendyrAttackActionResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameEngine {

  private final ExecutorService executorService;

  @Getter
  private final NotificationManager notificationManager;

  private final GameService gameService;

  @Getter
  private final CombatEntity combatEntity;

  @Getter
  private boolean gameOver;
  @Getter
  private boolean gameWon;

  @Builder
  GameEngine(@NonNull String host, int port) {
    this.gameService = GameService.builder().host(host).port(port).build();
    this.combatEntity = CombatEntity.builder().build();
    this.gameOver = false;
    this.gameWon = false;

    this.executorService = Executors.newSingleThreadExecutor();
    this.notificationManager = NotificationManager.builder().build();
  }

  public void load() {
    this.gameService.start();

    executorService.submit(() -> {
      System.out.println("Loading game...");
      try {
        System.out.println("Loading initial state...");
        this.gameService.getGameClient().forceLoad(this.combatEntity);
        System.out.println("Initial state loaded");
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
      try {
        System.out.println("Registering for game change...");
        this.gameService.getGameClient().register(this.combatEntity);
        System.out.println("Registered");
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
      System.out.println("Game is loaded");
    });
  }

  public void dispose() {
    this.gameService.stop();
    this.executorService.shutdown();
  }

  public boolean isLoaded() {
    return this.combatEntity.isLoaded();
  }

  public void move(CharacterEntity character, Vector2 newPosition) {
    if (!Objects.equals(combatEntity.getCurrentCharacter(), character)) {
      throw new IllegalStateException("Character move not allowed; it's not your turn");
    }
    if (newPosition.dst2(character.getPosition()) > Math.pow(character.getSpeed(), 2)) {
      throw new IllegalStateException("Character can't move further");
    }

    executorService.submit(() -> {
      try {
        System.out.println("Send move request...");
        LendyrActionResult result = gameService.getGameClient().move(character.getId(), List.of(newPosition));
        System.out.println("Move request completed");

        switch (result.getType()) {
          case NOT_ENOUGH_ACTION -> {
            notificationManager.pushNotification(NotificationMessageFactory.warning("Minor action is already used"));
          }
          case NOT_ALLOWED -> {
            notificationManager.pushNotification(NotificationMessageFactory.warning("Not allowed: " + result.getError()));
          }
          case NOT_YOUR_TURN -> {
            notificationManager.pushNotification(NotificationMessageFactory.warning("You can't play, it's not your turn"));
          }
          case NOT_FOUND -> {
            notificationManager.pushNotification(NotificationMessageFactory.warning("Count not find the character to move"));
          }
          case UNKNOWN, NOT_IMPLEMENTED, UNEXPECTED, UNRECOGNIZED -> {
            notificationManager.pushNotification(NotificationMessageFactory.error(result.getError().getDescription()));
          }
          case SUCCESS -> {
            System.out.println("action is successful");
          }
        }
      } catch (RuntimeException e) {
        notificationManager.pushNotification(NotificationMessage.builder().notificationType(NotificationType.ERROR).message(e.getMessage()).build());
      }
    });
  }

  public int attack(CharacterEntity from, CharacterEntity to) {
    // executorService.submit(() -> {
    System.out.println(from.getName() + " attack " + to.getName());
    try {
      LendyrAttackActionResult result = gameService.getGameClient().attack(from.getId(), to.getId());
      System.out.println("Attack result: " + result.getRawDamage() + " - " + result.getMitigatedDamage() + " - " + result.getAttackResult().getStunDie());
      return result.getMitigatedDamage();
    } catch (RuntimeException e) {
      notificationManager.pushNotification(NotificationMessageFactory.warning(e.getMessage()));
    }
    return 0;
    // });
  }

  public void endCharacterTurn() {
    executorService.submit(() -> {
      gameService.getGameClient().endPlayerTurn();
    });
  }
}
