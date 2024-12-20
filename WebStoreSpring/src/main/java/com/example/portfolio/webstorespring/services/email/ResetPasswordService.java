package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.accounts.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    @Transactional
    public Map<String, Object> resetPasswordByEmail(String email) {
        Account account = accountService.findByEmail(email);

        ConfirmationToken savedToken = confirmationTokenService.create(account);
        emailSenderService.sendEmail(
                NotificationType.RESET_PASSWORD,
                account.getEmail(),
                savedToken.getToken()
        );
        return Map.of("message", "Sent reset password link to your email");
    }

    @Transactional
    public Map<String, Object> confirmResetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        return confirmationTokenService.confirmTokenAndExecute(
                token,
                account -> accountService.setNewAccountPassword(account, resetPasswordRequest.password()),
                "Your new password has been saved"
        );
    }
}
