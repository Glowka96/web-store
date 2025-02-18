package com.example.portfolio.webstorespring.services.emails.registrations;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService extends AbstractConfirmEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    private static final String RESPONSE_MESSAGE = "Verify your email address using the link in your email.";

    RegistrationService(EmailSenderService emailSenderService,
                        AccountConfTokenService confirmationTokenService,
                        AccountService accountService) {
        super(emailSenderService, confirmationTokenService);
        this.accountService = accountService;
    }

    @Transactional
    public ResponseMessageDTO register(RegistrationRequest request) {
        Account account = accountService.save(request);
        sendConfirmationEmail(account, EmailType.CONFIRM_EMAIL);
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }

    @Transactional
    public ResponseMessageDTO confirm(String token) {
        return confirmTokenOrResend(token, EmailType.RESTORE_EMAIL);
    }

    @Override
    protected void executeAfterConfirm(Account ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
    }
}
