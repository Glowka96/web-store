package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ConfirmEmailNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

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
        return "To confirm your account, please click here: \n"
               + linkProvider.getEmail();
    }
}
