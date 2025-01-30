package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.emails.strategy.NotificationStrategy;
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
               + linkProvider.getEmail() + "%s";
    }
}
