package com.example.portfolio.webstorespring.controllers.emails.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class ReconfirmEmailNotificationStrategy implements NotificationStrategy{
    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RECONFIRM_EMAIL;
    }

    @Override
    public String getEmailTitle() {
        return "Confirm email address.";
    }

    @Override
    public String getEmailMessage() {
        return "To confirm your account, please click here: \n";
    }

    @Override
    public String getResponseMessage() {
        return "Your token is expired. Verify your email address using the new token link in your email.";
    }
}
