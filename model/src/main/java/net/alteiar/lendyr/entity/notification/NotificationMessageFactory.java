package net.alteiar.lendyr.entity.notification;

public interface NotificationMessageFactory {

  static NotificationMessage warning(String message) {
    return NotificationMessage.builder().notificationType(NotificationType.WARNING).message(message).build();
  }

  static NotificationMessage error(String message) {
    return NotificationMessage.builder().notificationType(NotificationType.ERROR).message(message).build();
  }

  static NotificationMessage information(String message) {
    return NotificationMessage.builder().notificationType(NotificationType.INFORMATION).message(message).build();
  }
}
