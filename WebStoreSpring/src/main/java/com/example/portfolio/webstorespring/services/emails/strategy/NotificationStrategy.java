package com.example.portfolio.webstorespring.services.emails.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;

public interface NotificationStrategy {

    NotificationType getNotificationType();

    String getEmailTitle();

    String getEmailMessage();
}
