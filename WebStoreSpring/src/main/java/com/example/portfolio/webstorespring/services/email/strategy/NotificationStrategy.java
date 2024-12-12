package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;

public interface NotificationStrategy {

    NotificationType getNotificationType();

    String getEmailTitle();

    String getEmailMessage();
}
