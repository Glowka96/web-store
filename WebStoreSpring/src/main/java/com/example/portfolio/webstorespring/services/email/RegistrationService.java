package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.accounts.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;
    private final ConfirmationLinkProvider confirmationLinkProvider;

    @Transactional
    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = accountService.save(registrationRequest);

        ConfirmationToken savedToken = confirmationTokenService.create(account);
        return emailSenderService.sendEmail(NotificationType.CONFIRM_EMAIL,
                account.getEmail(),
                getConfirmLinkWithToken(savedToken));
    }

    @Transactional
    public Map<String, Object> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationToken.getConfirmedAt() != null || Boolean.TRUE.equals(account.getEnabled())) {
            throw new EmailAlreadyConfirmedException();
        }

        if (Boolean.FALSE.equals(account.getEnabled()) && confirmationTokenService.isTokenExpired(confirmationToken)) {
            ConfirmationToken newToken = confirmationTokenService.create(account);
            confirmationTokenService.delete(confirmationToken);
            return emailSenderService.sendEmail(NotificationType.RECONFIRM_EMAIL,
                    account.getEmail(),
                    getConfirmLinkWithToken(newToken));
        }
        confirmationTokenService.setConfirmedAt(confirmationToken);
        accountService.setEnabledAccount(account);

        return Map.of("message", "Account confirmed.");
    }

    @NotNull
    private String getConfirmLinkWithToken(ConfirmationToken newToken) {
        return confirmationLinkProvider.getEmail() + newToken.getToken();
    }
}
