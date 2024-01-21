package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;

import java.time.Clock;
import java.time.LocalDateTime;

public class ConfirmationTokenBuilderHelper {

    public static ConfirmationToken createConfirmationToken(Account account) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .confirmedAt(DateForTestBuilderHelper.DATE_OF_CREATED)
                .expiresAt(DateForTestBuilderHelper.DATE_OF_CREATED.plusMinutes(15))
                .build();
    }

    public static ConfirmationToken createConfirmationToken(Account account, Clock clock) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .confirmedAt(LocalDateTime.now(clock))
                .expiresAt(LocalDateTime.now(clock).plusMinutes(15))
                .build();
    }

    public static ConfirmationToken createConfirmationTokenIsNotConfirmedAt(Account account, Clock clock) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .expiresAt(LocalDateTime.now(clock).plusMinutes(15))
                .build();
    }
}
