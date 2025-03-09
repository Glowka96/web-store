package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;

public class AccountConfTokenBuilderHelper {

    public static AccountConfToken createAccountConfToken(Account account, TokenDetails tokenDetails) {
        return AccountConfToken.builder()
                .account(account)
                .tokenDetails(tokenDetails)
                .build();
    }
}
