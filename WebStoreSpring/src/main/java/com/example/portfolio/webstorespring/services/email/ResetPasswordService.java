package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.accounts.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    @Value("${reset-password.confirmation.link}")
    private String confirmLink;

    @Transactional
    public Map<String, Object> resetPasswordByEmail(String email) {
        Account account = accountService.findAccountByEmail(email);

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(NotificationType.RESET_PASSWORD,
                account.getEmail(),
                confirmLink + savedToken.getToken());
    }

    @Transactional
    public Map<String, Object> confirmResetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        validateConfirmationToken(confirmationToken);

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        accountService.setNewAccountPassword(account, resetPasswordRequest.password());

        return Map.of("message", "Your new password has been saved");
    }

    private void validateConfirmationToken(ConfirmationToken confirmationToken) {
        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenConfirmedException();
        }

        if (confirmationTokenService.isTokenExpired(confirmationToken)) {
            throw new TokenExpiredException();
        }
    }
}