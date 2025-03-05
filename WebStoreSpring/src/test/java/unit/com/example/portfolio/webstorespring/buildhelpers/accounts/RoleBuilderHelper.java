package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.models.entity.accounts.Role;

public class RoleBuilderHelper {

    public static Role createUserRole() {
        return Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
    }
}
