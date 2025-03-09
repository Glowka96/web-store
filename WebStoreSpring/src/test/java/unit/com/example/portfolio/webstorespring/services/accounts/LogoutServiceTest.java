package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AuthTokenBuilderHelper.createAuthToken;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private AuthTokenRepository authTokenRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private LogoutService underTest;

    private static final String JWT = "7777";
    private static final String AUTH_HEADER = "Bearer " + JWT;
    private static final String AUTHORIZATION = "Authorization";

    @Test
    void shouldLogout() {
        Account account = make(a(BASIC_ACCOUNT));
        AuthToken authToken = createAuthToken(account, JWT);

        when(request.getHeader(AUTHORIZATION)).thenReturn(AUTH_HEADER);
        when(authTokenRepository.findByToken(any())).thenReturn(Optional.of(authToken));

        underTest.logout(request,response,authentication);

        verify(authTokenRepository).findByToken(JWT);
        verify(authTokenRepository).save(authToken);

        assertTrue(authToken.isExpired());
        assertTrue(authToken.isRevoked());
    }

    @Test
    void shouldNotLogout_whenInvalidAuthToken() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(JWT);

        underTest.logout(request,response,authentication);

        verify(authTokenRepository, never()).save(any(AuthToken.class));
        verify(authTokenRepository, never()).findByToken(anyString());
    }

    @Test
    void shouldNotLogout_whenNotAuthHeader() {
        when(request.getHeader(anyString())).thenReturn(null);

        underTest.logout(request, response, authentication);

        verify(authTokenRepository, never()).save(any(AuthToken.class));
        verify(authTokenRepository, never()).findByToken(anyString());
    }

    @Test
    void willThrowWhenNotFoundAuthToken() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(AUTH_HEADER);
        when(authTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.logout(request, response, authentication))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Authorization token with token 7777 not found");
    }
}
