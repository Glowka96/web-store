package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private AccountDetailsService accountDetailsService;
    @Mock
    private AuthTokenRepository tokenRepository;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private JwtAuthenticationFilter underTest;
    @Spy
    private MockHttpServletRequest request;
    @Spy
    private MockHttpServletResponse response;

    @Test
    void shouldDoFilterInternalWithValidToken() throws ServletException, IOException {
        // given
        String jwt = "valid-jwt-token";
        String authHeader = "Bearer " + jwt;

        Account account = Account.builder()
                .email("test@test.pl")
                .password("test123$")
                .accountRoles(AccountRoles.ROLE_USER)
                .build();
        UserDetails userDetails = new AccountDetails(account);

        // when
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(anyString())).thenReturn(account.getEmail());
        when(accountDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(new AuthToken()));

        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(request).getHeader("Authorization");
        verify(jwtService).extractUsername(jwt);
        verify(accountDetailsService).loadUserByUsername("test@test.pl");
        verify(jwtService).isTokenValid(jwt, userDetails);
        verify(tokenRepository).findByToken(jwt);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        // given
        String jwt = "invalidToken";
        String authHeader = "Bear" + jwt;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldDoFilterInternalWhenNoAuthHeader() throws ServletException, IOException {
        // given

        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
