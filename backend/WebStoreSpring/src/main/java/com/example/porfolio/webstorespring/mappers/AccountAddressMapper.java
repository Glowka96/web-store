package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface AccountAddressMapper {

    AccountAddressResponse mapToDto(AccountAddress address);

    AccountAddress mapToEntity(AccountAddressRequest accountAddressRequest);
}
