package com.example.portfolio.webstorespring.services.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
class AdminInitializer implements CommandLineRunner {

    private final AccountService accountService;
    @Override
    public void run(String... args) throws Exception {
        accountService.initializeAdminAccount();
    }
}
