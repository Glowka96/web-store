package com.example.portfolio.webstorespring.services.accounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleInitializeTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleInitialize roleInitialize;

    @Test
    void testRun() throws Exception {
        roleInitialize.run();

        verify(roleService, times(1)).initializeRole("ROLE_USER");
        verify(roleService, times(1)).initializeRole("ROLE_ADMIN");
    }
}
