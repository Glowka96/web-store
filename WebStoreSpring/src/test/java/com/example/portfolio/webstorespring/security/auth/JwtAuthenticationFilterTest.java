package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthTokenType;
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
        String jwt = "validToken";
        String authHeader = "Bearer " + jwt;

        Account account = Account.builder()
                .email("test@test.pl")
                .password("test123$")
                .accountRoles(AccountRoles.ROLE_USER)
                .build();
        AuthToken authToken = AuthToken.builder()
                .id(1L)
                .tokenType(AuthTokenType.BEARER)
                .token(jwt)
                .account(account)
                .expired(false)
                .revoked(false)
                .build();
        AccountDetails accountDetails = new AccountDetails(account);

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(anyString())).thenReturn(account.getEmail());
        when(accountDetailsService.loadUserByUsername(anyString())).thenReturn(accountDetails);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.ofNullable(authToken));
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
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
