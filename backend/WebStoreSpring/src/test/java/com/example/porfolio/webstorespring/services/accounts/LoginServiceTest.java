package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.model.dto.accounts.AuthenticationResponse;
import com.example.porfolio.webstorespring.model.dto.accounts.LoginRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.security.auth.AccountDetails;
import com.example.porfolio.webstorespring.security.auth.AuthService;
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
import org.springframework.security.core.userdetails.UserDetails;

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

    @Test
    void shouldLogin() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setFirstName("test");
        account.setLastName("abcd");

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        UserDetails userDetails = new AccountDetails(account);

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