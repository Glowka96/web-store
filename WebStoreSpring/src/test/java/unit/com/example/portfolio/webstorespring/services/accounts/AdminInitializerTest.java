package com.example.portfolio.webstorespring.services.accounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminInitializerTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private AdminInitializer adminInitializer;

    @Test
    void run() {
        adminInitializer.run();

        verify(accountService, times(1)).initializeAdminAccount();
    }
}
