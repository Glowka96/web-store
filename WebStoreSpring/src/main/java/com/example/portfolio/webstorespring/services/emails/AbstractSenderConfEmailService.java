package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.entities.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSenderConfEmailService<
        T extends ConfToken,
        O extends OwnerConfToken,
        S extends AbstractConfTokenService<T, O>> {

    protected final EmailSenderService emailSenderService;
    protected final S confirmationTokenService;

    protected void sendConfirmationEmail(O ownerToken, EmailType emailType) {
        T savedToken = confirmationTokenService.create(ownerToken, emailType);
        sendEmail(emailType, ownerToken.getEmail(), savedToken.getToken());
    }

    protected void sendEmail(EmailType emailType, String email, String ... tokensOrMessages) {
        emailSenderService.sendEmail(
                emailType,
                email,
                tokensOrMessages
        );
    }
}
