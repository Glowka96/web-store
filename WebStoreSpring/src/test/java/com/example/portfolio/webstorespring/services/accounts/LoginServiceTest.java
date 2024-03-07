package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.LoginRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AuthenticationResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.authentication.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthService authService;
    @InjectMocks
    private LoginService underTest;

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLogin() {
        // given
        Account account = AccountBuilderHelper.createAccountWithRoleUser();

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String jwtToken = "777";

        AuthenticationResponse response = new AuthenticationResponse(jwtToken);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.pl");
        loginRequest.setPassword("testpassword");

        // when
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authService.generateJwtToken(any())).thenReturn(jwtToken);

        AuthenticationResponse excepted = underTest.login(loginRequest);

        //then
        assertEquals(response, excepted);
    }
}
