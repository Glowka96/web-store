package com.example.portfolio.webstorespring.services.email.strategy.impl;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ResetPasswordNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

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
        return "To reset your password, please click here: \n"
               + linkProvider.getResetPassword();
    }
}
