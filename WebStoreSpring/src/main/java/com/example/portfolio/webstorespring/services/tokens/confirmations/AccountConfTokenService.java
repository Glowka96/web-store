package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Consumer;


@Service
@Slf4j
public class AccountConfTokenService extends AbstractConfTokenService<AccountConfToken, Account> {

    public AccountConfTokenService(ConfirmationTokenRepository<AccountConfToken> tokenRepository,
                                   TokenDetailsService tokenDetailsService,
                                   NotificationExpirationManager notificationExpirationManager) {
        super(tokenRepository, tokenDetailsService, notificationExpirationManager);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> confirmTokenAndExecute(String token,
                                                      Consumer<Account> confirmationConsumer,
                                                      String successMessage) {
        log.info("Starting confirming token: {}.", token);
        AccountConfToken tokenEntity = getByToken(token);
        log.debug("Extract related entity from: {}", token);
        Account relatedEntity = extractRelatedEntity(tokenEntity);
        tokenDetailsService.validate(tokenEntity.getTokenDetails());
        tokenDetailsService.setConfirmedAt(tokenEntity.getTokenDetails());
        confirmationConsumer.accept(relatedEntity);
        log.info("Operation successful, sending message: {}", successMessage);
        return Map.of("message", successMessage);
    }

    @Override
    protected AccountConfToken createTokenEntity(Account relatedEntity, TokenDetails tokenDetails) {
        return AccountConfToken.builder()
                .account(relatedEntity)
                .tokenDetails(tokenDetails)
                .build();
    }

    @Override
    public Account extractRelatedEntity(AccountConfToken tokenEntity) {
        return tokenEntity.getAccount();
    }
}
