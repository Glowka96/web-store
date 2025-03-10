package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountAddressRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountAddressResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.AccountAddress;

public interface AccountAddressMapper {

    static AccountAddressResponse mapToResponse(AccountAddress address) {
        return new AccountAddressResponse(
                address.getId(),
                address.getCity(),
                address.getPostcode(),
                address.getStreet()
                );
    }

    static AccountAddress mapToEntity(AccountAddressRequest accountAddressRequest) {
        return AccountAddress.builder()
                .city(accountAddressRequest.city())
                .street(accountAddressRequest.street())
                .postcode(accountAddressRequest.postcode())
                .build();
    }
}
