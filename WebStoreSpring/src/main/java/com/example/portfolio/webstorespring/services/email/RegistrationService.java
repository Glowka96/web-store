package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.confirmations.AccountConfTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AccountConfTokenService accountConfTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    private static final String MESSAGE = "message";

    @Transactional
    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = accountService.save(registrationRequest);

        AccountConfToken savedToken = accountConfTokenService.create(account);
        emailSenderService.sendEmail(
                NotificationType.CONFIRM_EMAIL,
                account.getEmail(),
                savedToken.getTokenDetails().getToken()
        );
        return Map.of(MESSAGE, "Verify your email address using the link in your email.");
    }

    @Transactional
    public Map<String, Object> confirmToken(String token) {
        AccountConfToken accountConfToken = accountConfTokenService.getByToken(token);
        Account account = accountConfToken.getAccount();

        if (accountConfToken.getTokenDetails().getConfirmedAt() != null || Boolean.TRUE.equals(account.getEnabled())) {
            throw new EmailAlreadyConfirmedException();
        }

        if (Boolean.FALSE.equals(account.getEnabled()) && accountConfTokenService.isTokenExpired(accountConfToken)) {
            AccountConfToken newToken = accountConfTokenService.create(account);
            accountConfTokenService.delete(accountConfToken);
            emailSenderService.sendEmail(
                    NotificationType.RECONFIRM_EMAIL,
                    account.getEmail(),
                    newToken.getTokenDetails().getToken()
            );
            return Map.of(MESSAGE, "Your token is expired. Verify your email address using the new token link in your email.");
        }
        accountConfTokenService.setConfirmedAt(accountConfToken);
        accountService.setEnabledAccount(account);

        return Map.of(MESSAGE, "Account confirmed.");
    }

}
