package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ResetPasswordService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    @Value("${reset-password.confirmation.link}")
    private String confirmLink;

    @Autowired
    public ResetPasswordService(
            ConfirmationTokenService confirmationTokenService,
            @Qualifier(value = "resetPasswordSender") EmailSenderService emailSenderService,
            AccountService accountService) {
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> resetPasswordByEmail(String email) {
        Account account = accountService.findAccountByEmail(email);

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(account.getEmail(),
                confirmLink + savedToken.getToken());
    }

    @Transactional
    public Map<String, Object> confirmResetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationTokenService.isConfirmed(confirmationToken)) {
            throw new TokenConfirmedException();
        }

        if (confirmationTokenService.isTokenExpired(confirmationToken)) {
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        accountService.setNewAccountPassword(account, resetPasswordRequest.password());

        return Map.of("message", "Your new password has been saved");
    }
}
