package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
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
