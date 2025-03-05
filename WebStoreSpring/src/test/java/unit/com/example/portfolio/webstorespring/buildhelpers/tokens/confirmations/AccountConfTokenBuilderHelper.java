package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;

public class AccountConfTokenBuilderHelper {

    public static AccountConfToken createAccountConfToken(Account account, TokenDetails tokenDetails) {
        return AccountConfToken.builder()
                .account(account)
                .tokenDetails(tokenDetails)
                .build();
    }
}
