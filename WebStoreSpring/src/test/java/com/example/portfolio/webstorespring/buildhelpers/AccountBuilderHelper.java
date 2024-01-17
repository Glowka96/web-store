package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;

import java.util.Set;

public class AccountBuilderHelper {

    private static final Role ROLE_USER = Role.builder()
            .id(1L)
            .name("ROLE_USER")
            .build();

    public static Account createAccountWithRoleUserAndAccountAddress() {
        AccountAddress accountAddress = AccountAddressBuilderHelper.createAccountAddress();
        return Account.builder()
                .id(1L)
                .firstName("Name")
                .lastName("Lastname")
                .email("test@test.pl")
                .password("password")
                .enabled(Boolean.TRUE)
                .imageUrl("test.pl/test.png")
                .roles(Set.of(ROLE_USER))
                .address(accountAddress)
                .build();
    }

    public static Account createAccountWithRoleUser() {
        return Account.builder()
                .id(1L)
                .firstName("Name")
                .lastName("Lastname")
                .email("test@test.pl")
                .password("password")
                .enabled(Boolean.TRUE)
                .imageUrl("test.pl/test.png")
                .roles(Set.of(ROLE_USER))
                .build();
    }
}
