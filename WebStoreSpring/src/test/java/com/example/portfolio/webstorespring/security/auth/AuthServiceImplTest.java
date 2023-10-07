package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.enums.AuthTokenType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthTokenRepository authTokenRepository;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl underTest;

    private Account account;
    private String jwtToken;
    private AuthToken authToken;

    @BeforeEach
    void initialization() {
        account = new Account();
        account.setId(1L);
        account.setFirstName("Test");
        account.setLastName("Jwt");
        account.setAccountRoles(AccountRoles.ROLE_USER);

        jwtToken = "777";

        authToken = AuthToken.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(AuthTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }

    @Test
    void shouldSaveAccountAuthToken() {
        underTest.saveAccountAuthToken(account, jwtToken);

        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void shouldRevokeAllUserAuthTokens() {
        // given
        List<AuthToken> authTokens = List.of(authToken);
        account.setAuthTokens(authTokens);

        // when
        when(authTokenRepository.findAllValidTokenByAccountId(anyLong())).thenReturn(authTokens);
        underTest.revokeAllUserAuthTokens(account);

        // then
        verify(authTokenRepository, times(1)).saveAll(authTokens);
        assertTrue(authToken.isRevoked());
        assertTrue(authToken.isExpired());
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetails() {
        // given
        UserDetails userDetails = new AccountDetails(account);

        // when
        when(jwtService.generateToken(any())).thenReturn(jwtToken);
        String excepted = underTest.generateJwtToken(userDetails);

        // then
        assertEquals(excepted, jwtToken);
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetailsAndClaims() {
        // given
        UserDetails userDetails = new AccountDetails(account);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_USER");

        // when
        when(jwtService.generateToken(anyMap(), any())).thenReturn(jwtToken);
        String excepted = underTest.generateJwtToken(extraClaims, userDetails);

        // then
        assertEquals(excepted, jwtToken);
    }
}
