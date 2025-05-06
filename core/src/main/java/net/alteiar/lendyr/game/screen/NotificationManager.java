package net.alteiar.lendyr.game.screen;

import lombok.Builder;
import net.alteiar.lendyr.entity.notification.NotificationMessage;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class NotificationManager {
  private final Queue<NotificationMessage> notifications;

  @Builder
  NotificationManager() {
    notifications = new LinkedList<>();
  }

  public void pushNotification(NotificationMessage message) {
    this.notifications.offer(message);
  }

  public Optional<NotificationMessage> popNotification() {
    return Optional.ofNullable(this.notifications.poll());
  }
}
