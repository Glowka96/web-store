package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.enums.AuthTokenType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtService jwtService;

    @Override
    public void saveAccountAuthToken(Account account, String jwtToken) {
        AuthToken token = AuthToken.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(AuthTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        authTokenRepository.save(token);
    }

    @Override
    public void revokeAllAccountAuthTokensByAccountId(Long accountId) {
        List<AuthToken> validUserTokens = authTokenRepository.findAllValidTokenByAccountId(accountId);
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(authToken -> {
            authToken.setExpired(true);
            authToken.setRevoked(true);
        });
        authTokenRepository.saveAll(validUserTokens);
    }

    @Override
    public String generateJwtToken(UserDetails userDetails) {
        return jwtService.generateToken(userDetails);
    }

    @Override
    public String generateJwtToken(Map<String, Object> extraClaims,
                                   UserDetails userDetails) {
        return jwtService.generateToken(extraClaims, userDetails);
    }
}
