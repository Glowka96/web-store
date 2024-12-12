package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
class ResetPasswordNotificationStrategy implements NotificationStrategy{
    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RESET_PASSWORD;
    }

    @Override
    public String getEmailTitle() {
        return "Complete reset password.";
    }

    @Override
    public String getEmailMessage() {
        return "To reset your password, please click here: \n";
    }
}
