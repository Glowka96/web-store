package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.LoginRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AuthenticationResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import com.example.portfolio.webstorespring.services.authentications.AuthService;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
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
        Account account = make(a(BASIC_ACCOUNT));

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String jwtToken = "777";

        AuthenticationResponse response = new AuthenticationResponse(jwtToken);

        LoginRequest loginRequest = new LoginRequest("test@test.pl","testpassword");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authService.generateJwtToken(any())).thenReturn(jwtToken);

        AuthenticationResponse excepted = underTest.login(loginRequest);

        assertEquals(response, excepted);
    }
}
