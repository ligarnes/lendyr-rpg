package net.alteiar.lendyr.entity.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {
  private NotificationType notificationType;
  private String message;
}
