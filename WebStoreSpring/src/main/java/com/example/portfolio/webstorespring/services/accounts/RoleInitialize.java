package com.example.portfolio.webstorespring.services.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitialize implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) {
        roleService.initializeRole("ROLE_USER");
        roleService.initializeRole("ROLE_ADMIN");
    }
}
