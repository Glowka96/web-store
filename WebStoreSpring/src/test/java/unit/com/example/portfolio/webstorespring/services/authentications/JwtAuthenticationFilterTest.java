package com.example.portfolio.webstorespring.services.authentications;

import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.accounts.AuthToken;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
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
    private static final String AUTHORIZATION = "Authorization";

    @Test
    void shouldDoFilterInternalWithValidToken() throws ServletException, IOException {
        String jwt = "valid-jwt-token";
        String authHeader = "Bearer " + jwt;

        Account account = make(a(BASIC_ACCOUNT));
        UserDetails userDetails = new AccountDetails(account);

        // when
        when(request.getHeader(AUTHORIZATION)).thenReturn(authHeader);
        when(jwtService.extractUsername(anyString())).thenReturn(account.getEmail());
        when(accountDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(new AuthToken()));

        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(request).getHeader(AUTHORIZATION);
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
        String authHeader = "Bearer" + jwt;

        when(request.getHeader(AUTHORIZATION)).thenReturn(authHeader);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldDoFilterInternalWhenNoAuthHeader() throws ServletException, IOException {
        // given
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
