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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "confirmationTokens", ignore = true)
    @Mapping(target = "authTokens", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "accountRoles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    Account mapToEntity(AccountRequest accountRequest);
}
