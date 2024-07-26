package com.example.portfolio.webstorespring.controllers.emails.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.impl.EmailSenderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public interface NotificationStrategy {

    NotificationType getNotificationType();

    String getEmailTitle();

    String getEmailMessage();

    String getResponseMessage();

    @Autowired
    default void registerMe(EmailSenderServiceImpl emailSenderService) {
        emailSenderService.registerNotification(
                getNotificationType(), this);
    }
}
