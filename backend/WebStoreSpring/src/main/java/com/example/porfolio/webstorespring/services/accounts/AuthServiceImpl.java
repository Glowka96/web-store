package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.porfolio.webstorespring.model.entity.accounts.AuthTokenType;
import com.example.porfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthTokenRepository tokenRepository;

    @Override
    public void saveAccountToken(Account account, String jwtToken) {
        AuthToken token = AuthToken.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(AuthTokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(Account account) {
        List<AuthToken> validUserTokens = tokenRepository.findAllValidTokenByAccountId(account.getId());
        if (validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(authToken -> {
            authToken.setExpired(true);
            authToken.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
