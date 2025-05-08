package net.alteiar.lendyr.game.battlemap;

import lombok.Builder;
import net.alteiar.lendyr.entity.model.AttackResult;
import net.alteiar.lendyr.entity.notification.NotificationMessage;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class EncounterController {
  private final Queue<NotificationMessage> notifications;
  private final Queue<AttackResult> damages;

  @Builder
  EncounterController() {
    notifications = new LinkedList<>();
    damages = new LinkedList<>();
  }

  public void pushNotification(NotificationMessage message) {
    this.notifications.offer(message);
  }

  public Optional<NotificationMessage> popNotification() {
    return Optional.ofNullable(this.notifications.poll());
  }

  public void pushDamage(AttackResult damage) {
    damages.offer(damage);
  }

  public Optional<AttackResult> popDamage() {
    return Optional.ofNullable(damages.poll());
  }
}
