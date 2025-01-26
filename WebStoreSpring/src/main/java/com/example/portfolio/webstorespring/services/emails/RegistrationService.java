package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegistrationService extends AbstractConfirmEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    RegistrationService(EmailSenderService emailSenderService,
                        AccountConfTokenService confirmationTokenService,
                        AccountService accountService) {
        super(emailSenderService, confirmationTokenService);
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> register(RegistrationRequest registrationRequest) {
        Account account = accountService.save(registrationRequest);
        sendConfirmationEmail(account, NotificationType.CONFIRM_EMAIL);
        return Map.of(RESPONSE_MESSAGE_KEY, "Verify your email address using the link in your email.");
    }

    @Transactional
    public Map<String, Object> confirm(String token) {
        return confirmTokenOrResend(token, NotificationType.RESTORE_EMAIL);
    }

    @Override
    protected void executeAfterConfirm(Account ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
    }
}
