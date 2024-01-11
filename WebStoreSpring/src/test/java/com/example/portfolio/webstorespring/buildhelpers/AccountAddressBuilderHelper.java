package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;

public class AccountAddressBuilderHelper {

    public static AccountAddress createAccountAddress() {
        return AccountAddress.builder()
                .id(1L)
                .city("Test city")
                .postcode("99-999")
                .street("Test street")
                .build();
    }
}
