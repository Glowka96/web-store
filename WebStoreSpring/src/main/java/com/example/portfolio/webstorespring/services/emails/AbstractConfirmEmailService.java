package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Slf4j
abstract class AbstractConfirmEmailService<
        T extends ConfToken,
        O extends OwnerConfToken,
        S extends AbstractConfTokenService<T, O>> extends AbstractSenderConfEmailService<T,O,S> {

    protected static final String RESPONSE_MESSAGE_KEY = "message";

    AbstractConfirmEmailService(EmailSenderService emailSenderService, S confirmationTokenService) {
        super(emailSenderService, confirmationTokenService);
    }

     protected Map<String, Object> confirmTokenOrResend(String token, NotificationType reNotificationType) {
        log.info("Starting confirmation token: {}", token);
        T confToken = confirmationTokenService.getByToken(token);
        O ownerToken = confirmationTokenService.extractRelatedEntity(confToken);

        confirmationTokenService.validateTokenConfirmedOrOwnerEnabled(confToken, ownerToken);

        if (confirmationTokenService.isOwnerDisabledAndTokenExpired(ownerToken, confToken)) {
            log.warn("Owner is disabled and token expired");
            return resendConfirmationEmail(reNotificationType, ownerToken, confToken);
        }

        log.debug("Setting up confirmed token and enabled owner");
        confirmationTokenService.setConfirmedAt(confToken);
        executeAfterConfirm(ownerToken);
        log.info("Operation successful, sending message");
        return Map.of(RESPONSE_MESSAGE_KEY, String.format("%s confirmed", ownerToken.getName()));
    }

    private @NotNull Map<String, Object> resendConfirmationEmail(NotificationType reNotificationType, O ownerToken, T confToken) {
        T savedToken = confirmationTokenService.create(ownerToken, reNotificationType);
        confirmationTokenService.delete(confToken);
        sendEmail(reNotificationType, ownerToken.getEmail(), savedToken.getToken());
        return Map.of(RESPONSE_MESSAGE_KEY, "Your token is expired. There is new confirmation link in your email.");
    }

    protected abstract void executeAfterConfirm(O ownerToken);
}
