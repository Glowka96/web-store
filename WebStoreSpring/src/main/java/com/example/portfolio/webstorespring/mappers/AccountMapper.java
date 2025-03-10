package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;

public interface AccountMapper {

    static AccountResponse mapToResponse(Account account) {
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
