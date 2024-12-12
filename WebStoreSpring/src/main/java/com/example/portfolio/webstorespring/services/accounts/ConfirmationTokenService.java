package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Clock clock;

    @Transactional
    public ConfirmationToken create(Account account) {
        log.info("Saving confirmation token for account ID: {}", account.getId());
        return createConfirmationToken(account, 15);
    }

    @Transactional
    public ConfirmationToken createWith7DaysExpires(Account account) {
        log.info("Saving confirmation token for account ID: {}", account.getId());
        return createConfirmationToken(account, 10_080);
    }

    public ConfirmationToken getByToken(String token) {
        log.debug("Finding confirmation token by token: {}", token);
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token", "token", token));
    }

    public boolean isTokenExpired(ConfirmationToken token) {
        log.debug("Validating if confirmation token has expired.");
        return token.getExpiresAt().isBefore(LocalDateTime.now(clock));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setConfirmedAt(ConfirmationToken token) {
        log.debug("Confirming token.");
        token.setConfirmedAt(LocalDateTime.now(clock));
    }

    public void delete(ConfirmationToken confirmationToken) {
        log.debug("Deleting confirmation token for token: {}", confirmationToken.getToken());
        confirmationTokenRepository.delete(confirmationToken);
    }

    private ConfirmationToken createConfirmationToken(Account account, long expiresMinute) {
        return confirmationTokenRepository.save(
                new ConfirmationToken(
                        UUID.randomUUID().toString(),
                        LocalDateTime.now(clock),
                        LocalDateTime.now(clock).plusMinutes(expiresMinute),
                        account
                ));
    }
}
