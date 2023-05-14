package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                OrderMapper.class,
                AccountAddressMapper.class
        }
)
public interface AccountMapper {

    @Mapping(target = "addressDto", source = "address")
    AccountResponse mapToDto(Account account);

    @Mapping(target = "address", source = "addressDto")
    Account mapToEntity(AccountRequest accountRequest);
}
