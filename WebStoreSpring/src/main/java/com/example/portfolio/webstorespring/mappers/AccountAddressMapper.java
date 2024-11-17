package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;

public class AccountAddressMapper {

    public static AccountAddressResponse mapToDto(AccountAddress address){
        return AccountAddressResponse.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .postcode(address.getPostcode())
                .build();
    }

    public static AccountAddress mapToEntity(AccountAddressRequest accountAddressRequest){
        return AccountAddress.builder()
                .city(accountAddressRequest.getCity())
                .street(accountAddressRequest.getStreet())
                .postcode(accountAddressRequest.getStreet())
                .build();
    }
}
