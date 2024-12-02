package com.example.portfolio.webstorespring.services.accounts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
@Slf4j
class AdminInitializer implements CommandLineRunner {

    private final AccountService accountService;
    @Override
    public void run(String... args) {
        log.info("Starting admin account initialization.");
        accountService.initializeAdminAccount();
        log.info("Admin account initialization completed.");
    }
}
