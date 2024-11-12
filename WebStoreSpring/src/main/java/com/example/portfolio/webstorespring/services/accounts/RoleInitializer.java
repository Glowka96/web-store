package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
class RoleInitializer implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) {
        roleService.initializeRole(RoleType.ROLE_USER.name());
        roleService.initializeRole(RoleType.ROLE_ADMIN.name());
    }
}
