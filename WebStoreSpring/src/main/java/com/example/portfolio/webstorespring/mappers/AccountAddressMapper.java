package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface AccountAddressMapper {

    AccountAddressResponse mapToDto(AccountAddress address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    AccountAddress mapToEntity(AccountAddressRequest accountAddressRequest);
}
