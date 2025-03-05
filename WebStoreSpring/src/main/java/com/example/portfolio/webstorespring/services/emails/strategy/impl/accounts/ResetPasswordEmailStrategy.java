package com.example.portfolio.webstorespring.services.emails.strategy.impl.accounts;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ResetPasswordEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getEmailType() {
        return EmailType.RESET_PASSWORD;
    }

    @Override
    public String getEmailTitle() {
        return "Complete reset password.";
    }

    @Override
    public String getEmailMessage() {
        return "To reset your password, please click here: \n"
               + linkProvider.getResetPassword() + "%s";
    }
}
