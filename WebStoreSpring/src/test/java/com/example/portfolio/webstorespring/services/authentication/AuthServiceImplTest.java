package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
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

import static com.example.portfolio.webstorespring.buildhelpers.AccountBuilderHelper.createAccountWithRoleUser;
import static com.example.portfolio.webstorespring.buildhelpers.AuthTokenBuilderHelper.createAuthToken;
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
        // given
        Account account = createAccountWithRoleUser();
        String jwtToken = "jwtToken";

        // when
        underTest.saveAccountAuthToken(account, jwtToken);

        // then
        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    void shouldRevokeAllUserAuthTokens() {
        // given
        Account account = createAccountWithRoleUser();
        AuthToken authToken = createAuthToken(account, "jwtToken");

        List<AuthToken> authTokens = List.of(authToken);
        account.setAuthTokens(authTokens);

        // when
        when(authTokenRepository.findAllValidTokenByAccountId(anyLong())).thenReturn(authTokens);
        underTest.revokeAllAccountAuthTokensByAccountId(account.getId());

        // then
        verify(authTokenRepository, times(1)).saveAll(authTokens);
        assertTrue(authToken.isRevoked());
        assertTrue(authToken.isExpired());
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetails() {
        // given
        Account account = createAccountWithRoleUser();
        UserDetails userDetails = new AccountDetails(account);

        // when
        when(jwtService.generateToken(any())).thenReturn("jwtToken");
        String excepted = underTest.generateJwtToken(userDetails);

        // then
        assertEquals("jwtToken", excepted);
    }

    @Test
    void shouldGenerateJwtTokenWhenGiveUserDetailsAndClaims() {
        // given
        Account account = createAccountWithRoleUser();
        UserDetails userDetails = new AccountDetails(account);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_USER");

        // when
        when(jwtService.generateToken(anyMap(), any())).thenReturn("jwtToken");
        String excepted = underTest.generateJwtToken(extraClaims, userDetails);

        // then
        assertEquals("jwtToken", excepted);
    }
}
