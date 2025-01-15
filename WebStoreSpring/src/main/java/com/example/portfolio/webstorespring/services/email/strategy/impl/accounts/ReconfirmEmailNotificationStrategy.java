package com.example.portfolio.webstorespring.services.email.strategy.impl.accounts;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReconfirmEmailNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RECONFIRM_EMAIL;
    }

    @Override
    public String getEmailTitle() {
        return "Re-confirm email address.";
    }

    @Override
    public String getEmailMessage() {
        return "To re-confirm your account, please click here: \n"
                + linkProvider.getEmail();
    }
}
