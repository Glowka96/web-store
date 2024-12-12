package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
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
public class BackupEmailService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;
    private final ConfirmationLinkProvider confirmationLinkProvider;

    public void sendBackupEmail(Account account) {
        ConfirmationToken savedToken = confirmationTokenService.createWithoutExpiresDate(account);
        emailSenderService.sendEmail(
                NotificationType.BACKUP_EMAIL,
                account.getEmail(),
                confirmationLinkProvider.getBackupEmail() + savedToken.getToken()
        );
    }

    @Transactional
    public Map<String, Object> confirmBackupEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
        Account account = confirmationToken.getAccount();

        if(confirmationToken.getConfirmedAt() != null) {
            throw new TokenConfirmedException();
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        accountService.restoreEmail(account);
        return Map.of("message", "Old account email restored");
    }
}
