package com.example.portfolio.webstorespring.controllers.emails.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class ConfirmEmailNotificationStrategy implements NotificationStrategy {

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.CONFIRM_EMAIL;
    }

    @Override
    public String getEmailTitle() {
        return "Confirm email address";
    }

    @Override
    public String getEmailMessage() {
        return "To confirm your account, please click here: \n";
    }

    @Override
    public String getResponseMessage() {
        return "Verify your email address using the link in your email.";
    }
}
