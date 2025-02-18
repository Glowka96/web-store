package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ConfirmEmailEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.CONFIRM_EMAIL;
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
