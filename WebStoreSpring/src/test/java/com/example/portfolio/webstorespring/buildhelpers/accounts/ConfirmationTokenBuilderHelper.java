package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;

import java.time.LocalDateTime;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class ConfirmationTokenBuilderHelper {

    public static ConfirmationToken createConfirmationTokenIsConfirmedAt(Account account) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .createdAt(LOCAL_DATE_TIME)
                .confirmedAt(LOCAL_DATE_TIME.plusMinutes(5))
                .expiresAt(LOCAL_DATE_TIME.plusMinutes(15))
                .build();
    }

    public static ConfirmationToken createConfirmationTokenIsNotConfirmedAt(Account account) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .createdAt(LOCAL_DATE_TIME)
                .expiresAt(LOCAL_DATE_TIME.plusMinutes(15))
                .build();
    }

    public static ConfirmationToken createConfirmationTokenIsNotConfirmedAt(Account account, LocalDateTime localDateTime) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .createdAt(localDateTime)
                .expiresAt(localDateTime.plusMinutes(15))
                .build();
    }
}
