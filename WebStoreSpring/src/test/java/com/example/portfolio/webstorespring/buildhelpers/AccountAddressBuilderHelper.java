package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;

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
        return AccountAddressRequest.builder()
                .city(CITY)
                .postcode(POSTCODE)
                .street(STREET)
                .build();
    }

    public static AccountAddressRequest createAccountAddressRequest(String city,
                                                                    String postcode,
                                                                    String street) {
        return AccountAddressRequest.builder()
                .city(city)
                .postcode(postcode)
                .street(street)
                .build();
    }

    public static AccountAddressResponse createAccountAddressResponse() {
        return AccountAddressResponse.builder()
                .id(1L)
                .city(CITY)
                .postcode(POSTCODE)
                .street(STREET)
                .build();
    }
}
