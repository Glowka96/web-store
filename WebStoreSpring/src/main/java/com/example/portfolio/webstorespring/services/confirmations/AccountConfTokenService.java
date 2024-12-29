package com.example.portfolio.webstorespring.services.confirmations;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.model.entity.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.accounts.AccountConfTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountConfTokenService {

    private final AccountConfTokenRepository accountConfTokenRepository;
    private final Clock clock;

    @Transactional
    public AccountConfToken create(Account account) {
        return createConfirmationToken(account, 15);
    }

    @Transactional
    public AccountConfToken createWith7DaysExpires(Account account) {
        return createConfirmationToken(account, 10_080);
    }

    public AccountConfToken getByToken(String token) {
        log.debug("Finding confirmation token by token: {}", token);
        return accountConfTokenRepository.findByTokenDetails_Token(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token", "token", token));
    }

    @Transactional
    public Map<String, Object> confirmTokenAndExecute(String token,
                                                      Consumer<Account> accountConsumer,
                                                      String successMessage) {
        log.info("Starting confirming token: {}.", token);
        AccountConfToken accountConfToken = getByToken(token);
        Account account = accountConfToken.getAccount();
        validateConfirmationToken(accountConfToken);
        setConfirmedAt(accountConfToken);
        accountConsumer.accept(account);
        log.info("Operation successful, sending message: {}", successMessage);
        return Map.of("message", successMessage);
    }

    public boolean isTokenExpired(AccountConfToken token) {
        log.debug("Validating if confirmation token has expired.");
        return token.getTokenDetails().getExpiresAt().isBefore(LocalDateTime.now(clock));
    }

    public void setConfirmedAt(AccountConfToken token) {
        log.debug("Confirming token: {}.", token.getTokenDetails().getToken());
        token.getTokenDetails().setConfirmedAt(LocalDateTime.now(clock));
        accountConfTokenRepository.save((token));
    }

    public void delete(AccountConfToken accountConfToken) {
        log.debug("Deleting confirmation token for token: {}", accountConfToken.getTokenDetails().getToken());
        accountConfTokenRepository.delete(accountConfToken);
    }

    private AccountConfToken createConfirmationToken(Account account, long expiresMinute) {
        return accountConfTokenRepository.save(
                AccountConfToken.builder()
                        .tokenDetails(TokenDetails.builder()
                                .token(UUID.randomUUID().toString())
                                .createdAt(LocalDateTime.now(clock))
                                .expiresAt(LocalDateTime.now(clock).plusMinutes(expiresMinute))
                                .build())
                        .account(account)
                        .build()
        );
    }

    private void validateConfirmationToken(AccountConfToken accountConfToken) {
        log.debug("Validating token: {}.", accountConfToken.getTokenDetails().getToken());
        if (accountConfToken.getTokenDetails().getConfirmedAt() != null) {
            log.debug("Invalid - token is confirmed.");
            throw new TokenConfirmedException();
        }

        if (isTokenExpired(accountConfToken)) {
            log.debug("Invalid token is expired");
            throw new TokenExpiredException();
        }
        log.debug("Valid successful.");
    }
}
