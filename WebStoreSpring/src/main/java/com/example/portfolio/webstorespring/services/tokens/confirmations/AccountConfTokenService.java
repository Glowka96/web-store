package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;


@Service
public class AccountConfTokenService extends AbstractConfTokenService<AccountConfToken, Account> {

    public AccountConfTokenService(ConfirmationTokenRepository<AccountConfToken> tokenRepository, TokenDetailsService tokenDetailsService) {
        super(tokenRepository, tokenDetailsService);
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
