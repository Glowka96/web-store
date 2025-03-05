package com.example.portfolio.webstorespring.services.emails.accountactions;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.emails.AbstractSenderConfEmailService;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestoreEmailService extends AbstractSenderConfEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    private static final String SUCCESS_RESPONSE_MESSAGE = "Old account email restored";


    public RestoreEmailService(EmailSenderService emailSenderService,
                               AccountConfTokenService confirmationTokenService,
                               AccountService accountService) {
        super(emailSenderService, confirmationTokenService);
        this.accountService = accountService;
    }

    public void sendRestoreEmail(Account account) {
        sendConfirmationEmail(account, EmailType.RESTORE_EMAIL);
    }

    @Transactional
    public ResponseMessageDTO confirm(String token) {
        return confirmationTokenService.confirmTokenAndExecute(
                token,
                accountService::restoreEmail,
                SUCCESS_RESPONSE_MESSAGE);
    }
}
