package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReconfirmEmailEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getEmailType() {
        return EmailType.RECONFIRM_EMAIL;
    }

    @Override
    public String getEmailTitle() {
        return "Re-confirm email address.";
    }

    @Override
    public String getEmailMessage() {
        return "To re-confirm your account, please click here: \n"
                + linkProvider.getEmail() + "%s";
    }
}
