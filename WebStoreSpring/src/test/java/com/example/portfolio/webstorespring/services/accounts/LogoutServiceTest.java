package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
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

    @Test
    void shouldLogout() {
        // given
        String jwt = "7777";
        String authHeader = "Bearer " + jwt;
        AuthToken authToken = new AuthToken();
        authToken.setToken(jwt);

        // when
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(authTokenRepository.findByToken(any())).thenReturn(Optional.of(authToken));

        underTest.logout(request,response,authentication);

        // then
        verify(authTokenRepository).findByToken(jwt);
        verify(authTokenRepository).save(authToken);

        assertTrue(authToken.isExpired());
        assertTrue(authToken.isRevoked());
    }

    @Test
    void shouldNotLogoutWhenInvalidAuthToken() {
        // given
        String jwt = "7777";

        // when
        when(request.getHeader("Authorization")).thenReturn(jwt);

        underTest.logout(request,response,authentication);

        //then
        verify(authTokenRepository, never()).save(any(AuthToken.class));
        verify(authTokenRepository, never()).findByToken(anyString());
    }

    @Test
    void shouldNotLogoutWhenNotAuthHeader() {
        // when
        when(request.getHeader(anyString())).thenReturn(null);

        underTest.logout(request, response, authentication);

        //then
        verify(authTokenRepository, never()).save(any(AuthToken.class));
        verify(authTokenRepository, never()).findByToken(anyString());
    }

    @Test
    void willThrowWhenNotFoundAuthToken() {
        // given
        String jwt = "Bearer 7777";
        AuthToken authToken = new AuthToken();
        authToken.setToken(jwt);

        // when
        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(authTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.logout(request, response, authentication))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Authorization token with token 7777 not found");
    }
}
