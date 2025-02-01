package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.enums.AuthTokenType;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtService jwtService;

    @Override
    public void saveAccountAuthToken(Account account, String jwtToken) {
        log.info("Creating new auth token for account ID: {}", account.getId());
        AuthToken token = AuthToken.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(AuthTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        authTokenRepository.save(token);
        log.info("Saved auth token for account ID: {}", account.getId());
    }

    @Override
    public void revokeAllAccountAuthTokensByAccountId(Long accountId) {
        log.info("Finding all valid token by account ID: {}", accountId);
        List<AuthToken> validUserTokens = authTokenRepository.findAllValidTokenByAccountId(accountId);
        if (validUserTokens.isEmpty()) {
            log.debug("List of valid account token is empty.");
            return;
        }

        log.info("Setting expired and revoked for all founded token.");
        validUserTokens.forEach(authToken -> {
            authToken.setExpired(true);
            authToken.setRevoked(true);
        });
        authTokenRepository.saveAll(validUserTokens);
        log.info("Saved auth tokens.");
    }

    @Override
    public String generateJwtToken(UserDetails userDetails) {
        log.info("Generating token.");
        return jwtService.generateToken(userDetails);
    }

    @Override
    public String generateJwtToken(Map<String, Object> extraClaims,
                                   UserDetails userDetails) {
        return jwtService.generateToken(extraClaims, userDetails);
    }
}
