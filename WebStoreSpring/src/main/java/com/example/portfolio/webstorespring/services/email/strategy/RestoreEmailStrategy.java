package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestoreEmailStrategy implements NotificationStrategy {

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
                + linkProvider.getRestoreEmail();
    }
}
