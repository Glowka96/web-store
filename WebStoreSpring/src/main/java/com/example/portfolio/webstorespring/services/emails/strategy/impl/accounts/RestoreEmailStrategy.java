package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RestoreEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.RESTORE_EMAIL;
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
