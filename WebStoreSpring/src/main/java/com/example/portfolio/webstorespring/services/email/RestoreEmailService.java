package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.confirmations.AccountConfTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestoreEmailService {

    private final AccountConfTokenService accountConfTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountService accountService;

    public void sendRestoreEmail(Account account) {
        log.info("Sending email for account " + account.getId());
        AccountConfToken savedToken = accountConfTokenService.createWith7DaysExpires(account);
        emailSenderService.sendEmail(
                NotificationType.RESTORE_EMAIL,
                account.getEmail(),
                savedToken.getTokenDetails().getToken()
        );
    }

    @Transactional
    public Map<String, Object> confirmRestoreEmail(String token) {
        return accountConfTokenService.confirmTokenAndExecute(
                token,
                accountService::restoreEmail,
                "Old account email restored"
        );
    }
}
