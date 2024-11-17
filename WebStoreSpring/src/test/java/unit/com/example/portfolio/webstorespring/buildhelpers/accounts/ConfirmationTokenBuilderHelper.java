package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.time.LocalDateTime;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class ConfirmationTokenBuilderHelper {

    public static final Property<ConfirmationToken, Long> ID = new Property<>();
    public static final Property<ConfirmationToken, String> TOKEN_NAME = new Property<>();
    public static final Property<ConfirmationToken, Account> ACCOUNT = new Property<>();
    public static final Property<ConfirmationToken, LocalDateTime> CREATED_AT = new Property<>();
    public static final Property<ConfirmationToken, LocalDateTime> CONFIRMED_AT = new Property<>();
    public static final Property<ConfirmationToken, LocalDateTime> EXPIRED_AT = new Property<>();

    public static final Instantiator<ConfirmationToken> BASIC_CONFIRMATION_TOKEN = lookup ->
            ConfirmationToken.builder()
                    .id(lookup.valueOf(ID, 1L))
                    .token(lookup.valueOf(TOKEN_NAME, "token"))
                    .account(lookup.valueOf(ACCOUNT, new Account()))
                    .createdAt(lookup.valueOf(CREATED_AT, LOCAL_DATE_TIME))
                    .confirmedAt(lookup.valueOf(CONFIRMED_AT, LOCAL_DATE_TIME))
                    .expiresAt(lookup.valueOf(EXPIRED_AT, LOCAL_DATE_TIME))
                    .build();
}
