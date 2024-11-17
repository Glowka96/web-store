package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;

public class AccountMapper {

    public static AccountResponse mapToDto(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .imageUrl(account.getImageUrl())
                .build();
    }

    public static Account mapToEntity(AccountRequest accountRequest) {
        return Account.builder()
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .imageUrl(accountRequest.getImageUrl())
                .build();
    }
}
