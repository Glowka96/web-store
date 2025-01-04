package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.confirmations.AccountConfTokenService;
import com.example.portfolio.webstorespring.services.confirmations.TokenDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RestoreEmailService extends SenderConfirmationEmailService<AccountConfToken, Account, AccountConfTokenService> {
    private final AccountService accountService;

    public RestoreEmailService(EmailSenderService emailSenderService,
                               TokenDetailsService tokenDetailsService,
                               AccountConfTokenService confirmationsService,
                               AccountService accountService) {
        super(emailSenderService, tokenDetailsService, confirmationsService);
        this.accountService = accountService;
    }

    public void sendRestoreEmail(Account account) {
        sendConfirmationEmail(account, NotificationType.RESTORE_EMAIL);
    }

    @Transactional
    public Map<String, Object> confirmRestoreEmail(String token) {
        return confirmationTokenService.confirmTokenAndExecute(
                token,
                accountService::restoreEmail,
                "Old account email restored");
    }
}
