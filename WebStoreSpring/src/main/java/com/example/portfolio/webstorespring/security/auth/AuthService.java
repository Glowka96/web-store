package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthService {
    void saveAccountAuthToken(Account account, String jwtToken);

    void revokeAllUserAuthTokens(Account account);

    String generateJwtToken(UserDetails userDetails);

    String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails);

    Boolean checkAuthorization(Long accountId, String authToken);
}
