package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.TokenDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Slf4j
abstract class AbstractConfirmEmailService<
        T extends ConfToken,
        O extends OwnerConfToken,
        S extends AbstractConfTokenService<T, O>> extends AbstractSenderConfEmailService<T,O,S> {

    private final TokenDetailsService tokenDetailsService;

    protected static final String RESPONSE_MESSAGE_KEY = "message";

    AbstractConfirmEmailService(EmailSenderService emailSenderService, S confirmationTokenService, TokenDetailsService tokenDetailsService) {
        super(emailSenderService, confirmationTokenService);
        this.tokenDetailsService = tokenDetailsService;
    }

     protected Map<String, Object> confirmTokenOrResend(String token, NotificationType reNotificationType) {
        log.info("Starting confirmation token: {}", token);
        T confToken = confirmationTokenService.getByToken(token);
        O ownerToken = confirmationTokenService.extractRelatedEntity(confToken);

        validateTokenConfirmedOrOwnerEnabled(confToken, ownerToken);

        if (isOwnerDisabledAndTokenExpired(ownerToken, confToken)) {
            log.debug("Owner is disabled and token expired");
            return resendConfirmationEmail(reNotificationType, ownerToken, confToken);
        }

        log.debug("Setting up confirmed token and enabled owner");
        tokenDetailsService.setConfirmedAt(confToken.getTokenDetails());
        executeAfterConfirm(ownerToken);
        log.info("Operation successful, sending message");
        return Map.of(RESPONSE_MESSAGE_KEY, String.format("%s confirmed", ownerToken.getName()));
    }

    private void validateTokenConfirmedOrOwnerEnabled(T confToken, O ownerToken) {
        log.debug("Checking if token confirmed or owner is enabled");
        if (confToken.getTokenDetails().getConfirmedAt() != null || Boolean.TRUE.equals(ownerToken.getEnabled())) {
            log.debug("Token confirmed or owner is enabled");
            throw new EmailAlreadyConfirmedException();
        }
    }

    private boolean isOwnerDisabledAndTokenExpired(O ownerToken, T confToken) {
        return Boolean.FALSE.equals(ownerToken.getEnabled()) && tokenDetailsService.isTokenExpired(confToken.getTokenDetails());
    }

    private @NotNull Map<String, Object> resendConfirmationEmail(NotificationType reNotificationType, O ownerToken, T confToken) {
        T savedToken = confirmationTokenService.create(ownerToken, reNotificationType);
        confirmationTokenService.delete(confToken);
        sendEmail(reNotificationType, ownerToken.getEmail(), savedToken.getToken());
        return Map.of(RESPONSE_MESSAGE_KEY, "Your token is expired. Verify your email address using the new token link in your email.");
    }

    protected abstract void executeAfterConfirm(O ownerToken);
}
