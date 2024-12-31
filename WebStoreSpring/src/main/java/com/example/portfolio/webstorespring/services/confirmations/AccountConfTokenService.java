package com.example.portfolio.webstorespring.services.confirmations;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.model.entity.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.confirmations.ConfirmationTokenRepository;
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
    protected Account extractRelatedEntity(AccountConfToken tokenEntity) {
        return tokenEntity.getAccount();
    }
}
