package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class AbstractSenderConfEmailService<
        T extends ConfToken,
        O extends OwnerConfToken,
        S extends AbstractConfTokenService<T, O>> {

    protected final EmailSenderService emailSenderService;
    protected final S confirmationTokenService;

    protected void sendConfirmationEmail(O ownerToken, NotificationType notificationType) {
        T savedToken = confirmationTokenService.create(ownerToken, notificationType);
        sendEmail(notificationType, ownerToken.getEmail(), savedToken.getToken());
    }

    protected void sendEmail(NotificationType notificationType, String email, String token) {
        emailSenderService.sendEmail(
                notificationType,
                email,
                token
        );
    }
}
