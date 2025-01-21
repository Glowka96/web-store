package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ResetPasswordService extends AbstractSenderConfEmailService<AccountConfToken, Account, AccountConfTokenService>{

    private final AccountService accountService;

    public ResetPasswordService(EmailSenderService emailSenderService,
                                AccountConfTokenService confirmationTokenService,
                                AccountService accountService) {
        super(emailSenderService, confirmationTokenService);
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> sendResetPasswordLinkByEmail(String email) {
        Account account = accountService.findByEmail(email);
        sendConfirmationEmail(account,NotificationType.RESET_PASSWORD);
        return Map.of("message", "Sent reset password link to your email");
    }

    @Transactional
    public Map<String, Object> confirm(ResetPasswordRequest resetPasswordRequest, String token) {
        return confirmationTokenService.confirmTokenAndExecute(
                token,
                account -> accountService.setNewAccountPassword(account, resetPasswordRequest.password()),
                "Your new password has been saved"
        );
    }
}
