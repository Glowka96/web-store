package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
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

    @Mapping(target = "ordersDto", source = "orders")
    AccountDto mapToDto(Account account);

    @Mapping(target = "orders", source = "ordersDto")
    Account mapToEntity(AccountDto accountDto);
}
