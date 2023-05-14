package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface AccountAddressMapper {

    AccountAddressDto mapToDto(AccountAddress address);

    AccountAddress mapToEntity(AccountAddressDto accountAddressDto);
}
