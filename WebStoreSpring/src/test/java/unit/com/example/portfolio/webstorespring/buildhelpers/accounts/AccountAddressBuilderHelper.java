package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountAddressRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountAddressResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.AccountAddress;

public class AccountAddressBuilderHelper {

    private static final String CITY = "Testcity";
    private static final String POSTCODE = "99-999";
    private static final String STREET = "Test-street 5";


    public static AccountAddress createAccountAddress() {
        return AccountAddress.builder()
                .id(1L)
                .city(CITY)
                .postcode(POSTCODE)
                .street(STREET)
                .build();
    }

    public static AccountAddressRequest createAccountAddressRequest() {
        return new AccountAddressRequest(CITY, POSTCODE, STREET);
    }

    public static AccountAddressResponse createAccountAddressResponse() {
        return new AccountAddressResponse(1L, CITY, STREET, POSTCODE);
    }
}
