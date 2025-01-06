package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.TokenDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ResetPasswordService extends SenderConfirmationEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    public ResetPasswordService(EmailSenderService emailSenderService,
                                TokenDetailsService tokenDetailsService,
                                AccountConfTokenService confirmationsService,
                                AccountService accountService) {
        super(emailSenderService, tokenDetailsService, confirmationsService);
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> resetPasswordByEmail(String email) {
        Account account = accountService.findByEmail(email);
        sendConfirmationEmail(account, NotificationType.RESET_PASSWORD);
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
