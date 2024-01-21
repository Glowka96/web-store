package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.enums.AuthTokenType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;

public class AuthTokenBuilderHelper {

    public static AuthToken createAuthToken(Account account, String token) {
        return AuthToken.builder()
                .id(1L)
                .tokenType(AuthTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .account(account)
                .token(token)
                .build();
    }
}
