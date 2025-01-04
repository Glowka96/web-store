package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.confirmations.AccountConfTokenService;
import com.example.portfolio.webstorespring.services.confirmations.TokenDetailsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegistrationService extends SenderConfirmationEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    @Resource
    private SenderConfirmationEmailService<AccountConfToken, Account, AccountConfTokenService> senderConfirmationEmailService;

    public RegistrationService(EmailSenderService emailSenderService, TokenDetailsService tokenDetailsService, AccountConfTokenService confirmationsService, AccountService accountService) {
        super(emailSenderService, tokenDetailsService, confirmationsService);
        this.accountService = accountService;
    }

    @Transactional
    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = accountService.save(registrationRequest);
        sendConfirmationEmail(account, NotificationType.CONFIRM_EMAIL);
        return Map.of(MESSAGE, "Verify your email address using the link in your email.");
    }

    @Transactional
    public Map<String, Object> confirmToken(String token) {
        return senderConfirmationEmailService.confirmTokenOrResend(token, NotificationType.RESTORE_EMAIL);
    }
}
