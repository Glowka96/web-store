package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface AccountAddressMapper {

    @Mapping(target = "accountDto", source = "account")
    AccountAddressDto mapToDto(AccountAddress address);

    @Mapping(target = "account", source = "accountDto")
    AccountAddress mapToEntity(AccountAddressDto accountAddressDto);
}
