package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;

import java.util.Map;

public interface EmailSenderService {
     Map<String, Object> sendEmail(NotificationType notificationType,
                                   String email,
                                   String confirmLinkWithToken);

     void registerNotification(NotificationType notificationType,
                               NotificationStrategy notificationStrategy);
}
