package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
@Slf4j
class RoleInitializer implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) {
        log.info("Starting roles initialization.");
        for (RoleType roleType : RoleType.values()) {
            roleService.initializeRole(roleType.name());
        }
        log.info("Roles initialization completed.");
    }
}
