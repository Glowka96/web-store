package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entities.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public abstract class AbstractConfirmEmailService<
        T extends ConfToken,
        O extends OwnerConfToken,
        S extends AbstractConfTokenService<T, O>> extends AbstractSenderConfEmailService<T,O,S> {

    private static final String RESEND_RESPONSE_MESSAGE = "Your token is expired. There is new confirmation link in your email.";

    protected AbstractConfirmEmailService(EmailSenderService emailSenderService, S confirmationTokenService) {
        super(emailSenderService, confirmationTokenService);
    }

     protected ResponseMessageDTO confirmTokenOrResend(String token, EmailType reEmailType) {
        log.info("Starting confirmation token: {}", token);
        T confToken = confirmationTokenService.getByToken(token);
        O ownerToken = confirmationTokenService.extractRelatedEntity(confToken);

        confirmationTokenService.validateTokenConfirmedOrOwnerEnabled(confToken, ownerToken);

        if (confirmationTokenService.isOwnerDisabledAndTokenExpired(ownerToken, confToken)) {
            log.warn("Owner is disabled and token expired");
            return resendConfirmationEmail(reEmailType, ownerToken, confToken);
        }

        log.debug("Setting up confirmed token and enabled owner");
        confirmationTokenService.setConfirmedAt(confToken);
        executeAfterConfirm(ownerToken);
        log.info("Operation successful, sending message");
        return new ResponseMessageDTO(String.format("%s confirmed.", ownerToken.getName()));
    }

    private @NotNull ResponseMessageDTO resendConfirmationEmail(EmailType reEmailType, O ownerToken, T confToken) {
        T savedToken = confirmationTokenService.create(ownerToken, reEmailType);
        confirmationTokenService.delete(confToken);
        sendEmail(reEmailType, ownerToken.getEmail(), savedToken.getToken());
        return new ResponseMessageDTO(RESEND_RESPONSE_MESSAGE);
    }

    protected abstract void executeAfterConfirm(O ownerToken);
}
