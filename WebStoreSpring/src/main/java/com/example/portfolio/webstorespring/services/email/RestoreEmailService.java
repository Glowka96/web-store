package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
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
public class RestoreEmailService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    public void sendBackupEmail(Account account) {
        ConfirmationToken savedToken = confirmationTokenService.createWith7DaysExpires(account);
        emailSenderService.sendEmail(
                NotificationType.RESTORE_EMAIL,
                account.getEmail(),
                savedToken.getToken()
        );
    }

    @Transactional
    public Map<String, Object> confirmBackupEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
        Account account = confirmationToken.getAccount();

        confirmationTokenService.validateConfirmationToken(confirmationToken);

        confirmationTokenService.setConfirmedAt(confirmationToken);
        accountService.restoreEmail(account);
        return Map.of("message", "Old account email restored");
    }
}
