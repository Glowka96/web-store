package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;

public interface AccountMapper {

    static AccountResponse mapToDto(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getImageUrl()
        );
    }

    static Account mapToEntity(AccountRequest accountRequest) {
        return Account.builder()
                .firstName(accountRequest.firstName())
                .lastName(accountRequest.lastName())
                .imageUrl(accountRequest.imageUrl())
                .build();
    }
}
