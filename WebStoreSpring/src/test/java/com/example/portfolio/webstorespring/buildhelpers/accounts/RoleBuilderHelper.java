package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;

public class RoleBuilderHelper {

    public static Role createRole() {
        return Role.builder()
                .id(1L)
                .name("Test")
                .build();
    }
}
