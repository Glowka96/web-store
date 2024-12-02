package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;

public interface AccountAddressMapper {

    static AccountAddressResponse mapToDto(AccountAddress address) {
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
