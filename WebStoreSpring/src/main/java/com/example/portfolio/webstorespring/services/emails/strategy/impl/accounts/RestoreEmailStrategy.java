package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.emails.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RestoreEmailStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RESTORE_EMAIL;
    }

    @Override
    public String getEmailTitle() {
        return "Restore your email.";
    }

    @Override
    public String getEmailMessage() {
        return "To restore your email, please click here: \n"
                + linkProvider.getRestoreEmail() + "%s";
    }
}
