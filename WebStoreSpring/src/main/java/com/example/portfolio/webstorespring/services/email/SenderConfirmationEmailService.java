package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.BaseConfToken;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.TokenDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
abstract class SenderConfirmationEmailService<T extends BaseConfToken, O extends OwnerConfToken, S extends AbstractConfTokenService<T, O>> {

    private final EmailSenderService emailSenderService;
    private final TokenDetailsService tokenDetailsService;
    protected final S confirmationTokenService;

    protected static final String MESSAGE = "message";

    protected void sendConfirmationEmail(O ownerToken, NotificationType notificationType) {
        T savedToken = confirmationTokenService.create(ownerToken, notificationType.getExpiresMinute());
        sendEmail(notificationType, ownerToken.getEmail(), savedToken.getToken());
    }

    public Map<String, Object> confirmTokenOrResend(String token, NotificationType notificationType) {
        log.info("Starting confirmation token: {}", token);
        T confToken = confirmationTokenService.getByToken(token);
        O ownerToken = confirmationTokenService.extractRelatedEntity(confToken);

        validateTokenConfirmedOrOwnerEnabled(confToken, ownerToken);

        if (isOwnerDisabledAndTokenExpired(ownerToken, confToken)) {
            log.debug("Owner is disabled and token expired");
            return resendConfirmationEmail(notificationType, ownerToken, confToken);
        }

        log.debug("Setting up confirmed token and enabled owner");
        tokenDetailsService.setConfirmedAt(confToken.getTokenDetails());
        ownerToken.setEnabled(Boolean.TRUE);
        log.info("Operation successful, sending message");
        return Map.of(MESSAGE, String.format("%s confirmed", ownerToken.getName()));
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

    private @NotNull Map<String, Object> resendConfirmationEmail(NotificationType notificationType, O ownerToken, T confToken) {
        T savedToken = confirmationTokenService.create(ownerToken, notificationType.getExpiresMinute());
        confirmationTokenService.delete(confToken);
        sendEmail(notificationType, ownerToken.getEmail(), savedToken.getToken());
        return Map.of(MESSAGE, "Your token is expired. Verify your email address using the new token link in your email.");
    }

    private void sendEmail(NotificationType notificationType, String email, String tokenDetails) {
        emailSenderService.sendEmail(
                notificationType,
                email,
                tokenDetails
        );
    }
}
