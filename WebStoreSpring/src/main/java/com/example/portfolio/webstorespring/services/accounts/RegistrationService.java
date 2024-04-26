package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegistrationService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;


    @Value("${email.confirmation.link}")
    private String confirmLink;

    @Autowired
    public RegistrationService(ConfirmationTokenService confirmationTokenService,
                               @Qualifier(value = "confirmEmailSender") EmailSenderService emailSenderService,
                               AccountService accountService) {
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = accountService.saveAccount(registrationRequest);

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(account.getEmail(),
                getConfirmLinkWithToken(savedToken));
    }

    @Transactional
    public Map<String, Object> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException();
        }

        if (Boolean.TRUE.equals(!account.getEnabled()) && confirmationTokenService.isTokenExpired(confirmationToken)) {
            ConfirmationToken newToken = confirmationTokenService.createConfirmationToken(account);
            confirmationTokenService.deleteConfirmationToken(confirmationToken);
            return emailSenderService.sendEmail(account.getEmail(),
                    getConfirmLinkWithToken(newToken));
        }

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        accountService.setEnabledAccount(account);

        return Map.of("message", "Account confirmed");
    }

    @NotNull
    private String getConfirmLinkWithToken(ConfirmationToken newToken) {
        return confirmLink + newToken.getToken();
    }
}
