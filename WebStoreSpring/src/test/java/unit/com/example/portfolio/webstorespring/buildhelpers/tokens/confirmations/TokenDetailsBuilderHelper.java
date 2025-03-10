package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.time.LocalDateTime;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class TokenDetailsBuilderHelper {

    public static final Property<TokenDetails, Long> ID = new Property<>();
    public static final Property<TokenDetails, String> TOKEN_NAME = new Property<>();
    public static final Property<TokenDetails, LocalDateTime> CREATED_AT = new Property<>();
    public static final Property<TokenDetails, LocalDateTime> EXPIRES_AT = new Property<>();
    public static final Property<TokenDetails, LocalDateTime> CONFIRMED_AT = new Property<>();

    public static final Instantiator<TokenDetails> BASIC_TOKEN_DETAILS = lookup ->
            TokenDetails.builder()
                    .token(lookup.valueOf(TOKEN_NAME, "token"))
                    .createdAt(lookup.valueOf(CREATED_AT, LOCAL_DATE_TIME))
                    .confirmedAt(lookup.valueOf(CONFIRMED_AT, LOCAL_DATE_TIME.plusMinutes(10)))
                    .expiresAt(lookup.valueOf(EXPIRES_AT, LOCAL_DATE_TIME.plusMinutes(15)))
                    .build();
}
