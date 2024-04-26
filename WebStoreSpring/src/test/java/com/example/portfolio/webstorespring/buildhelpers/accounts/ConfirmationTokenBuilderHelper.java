package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;

public class ConfirmationTokenBuilderHelper {

    public static ConfirmationToken createConfirmationTokenIsConfirmedAt(Account account) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .createdAt(DateForTestBuilderHelper.ZONED_DATE_TIME.toLocalDateTime())
                .confirmedAt(DateForTestBuilderHelper.ZONED_DATE_TIME.toLocalDateTime().plusMinutes(5))
                .expiresAt(DateForTestBuilderHelper.ZONED_DATE_TIME.toLocalDateTime().plusMinutes(15))
                .build();
    }

    public static ConfirmationToken createConfirmationTokenIsNotConfirmedAt(Account account) {
        return ConfirmationToken.builder()
                .id(1L)
                .token("token")
                .account(account)
                .createdAt(DateForTestBuilderHelper.ZONED_DATE_TIME.toLocalDateTime())
                .expiresAt(DateForTestBuilderHelper.ZONED_DATE_TIME.toLocalDateTime().plusMinutes(15))
                .build();
    }
}
