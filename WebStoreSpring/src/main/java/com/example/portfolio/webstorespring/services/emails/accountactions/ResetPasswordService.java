package com.example.portfolio.webstorespring.services.emails.accountactions;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.ResetPasswordRequest;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.emails.AbstractSenderConfEmailService;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetPasswordService extends AbstractSenderConfEmailService<AccountConfToken, Account, AccountConfTokenService> {

    private final AccountService accountService;

    private static final String SEND_RESPONSE_MESSAGE = "Sent reset password link to your email";
    private static final String SUCCESS_RESPONSE_MESSAGE = "Your new password has been saved";

    public ResetPasswordService(EmailSenderService emailSenderService,
                                AccountConfTokenService confirmationTokenService,
                                AccountService accountService) {
        super(emailSenderService, confirmationTokenService);
        this.accountService = accountService;
    }

    @Transactional
    public ResponseMessageDTO sendResetPasswordLinkByEmail(String email) {
        Account account = accountService.findByEmail(email);
        sendConfirmationEmail(account, EmailType.RESET_PASSWORD);
        return new ResponseMessageDTO(SEND_RESPONSE_MESSAGE);
    }

    @Transactional
    public ResponseMessageDTO confirm(ResetPasswordRequest request, String token) {
        return confirmationTokenService.confirmTokenAndExecute(
                token,
                account -> accountService.setNewAccountPassword(account, request.password()),
                SUCCESS_RESPONSE_MESSAGE
        );
    }
}
