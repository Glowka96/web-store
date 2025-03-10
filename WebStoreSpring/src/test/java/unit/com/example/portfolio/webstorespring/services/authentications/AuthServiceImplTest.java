package com.example.portfolio.webstorespring.services.authentications;

import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AuthTokenBuilderHelper.createAuthToken;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
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

    @Test
    void shouldSaveAccountAuthToken() {
        Account account = make(a(BASIC_ACCOUNT));
        String jwtToken = "jwtToken";

        underTest.saveAccountAuthToken(account, jwtToken);

        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void shouldRevokeAllUserAuthTokens() {
        Account account = make(a(BASIC_ACCOUNT));
        AuthToken authToken = createAuthToken(account, "jwtToken");

        List<AuthToken> authTokens = List.of(authToken);
        account.setAuthTokens(authTokens);

        when(authTokenRepository.findAllValidTokenByAccountId(anyLong())).thenReturn(authTokens);
        underTest.revokeAllAccountAuthTokensByAccountId(account.getId());

        verify(authTokenRepository, times(1)).saveAll(authTokens);
        assertTrue(authToken.isRevoked());
        assertTrue(authToken.isExpired());
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetails() {
        Account account = make(a(BASIC_ACCOUNT));
        UserDetails userDetails = new AccountDetails(account);

        when(jwtService.generateToken(any())).thenReturn("jwtToken");
        String excepted = underTest.generateJwtToken(userDetails);

        assertEquals("jwtToken", excepted);
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetailsAndClaims() {
        Account account = make(a(BASIC_ACCOUNT));
        UserDetails userDetails = new AccountDetails(account);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_USER");

        when(jwtService.generateToken(anyMap(), any())).thenReturn("jwtToken");
        String excepted = underTest.generateJwtToken(extraClaims, userDetails);

        assertEquals("jwtToken", excepted);
    }
}
