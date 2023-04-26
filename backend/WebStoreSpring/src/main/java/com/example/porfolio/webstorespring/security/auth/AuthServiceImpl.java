package com.example.porfolio.webstorespring.security.auth;

import com.example.porfolio.webstorespring.exceptions.AccountCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.porfolio.webstorespring.model.entity.accounts.AuthTokenType;
import com.example.porfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
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
    public void revokeAllUserAuthTokens(Account account) {
        List<AuthToken> validUserTokens = authTokenRepository.findAllValidTokenByAccountId(account.getId());
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

    @Override
    public Boolean checkAuthorization(Long accountId, String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return false;
        }

        String authToken = authHeader.substring(7);

        Account foundAccountByAuthToken = findAccountByAuthToken(authToken);
        Account account = getAccountPrincipal().getAccount();

        if (!foundAccountByAuthToken.getId().equals(accountId) &&
                !account.getId().equals(accountId)) {
            throw new AccountCanNotModifiedException();
        }

        return true;
    }

    private Account findAccountByAuthToken(String authToken) {
        return authTokenRepository.findByToken(authToken)
                .orElseThrow(() -> new ResourceNotFoundException("Authorization token", "token", authToken))
                .getAccount();
    }

    private AccountDetails getAccountPrincipal() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
