package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthService {
    void saveAccountAuthToken(Account account, String jwtToken);

    void revokeAllAccountAuthTokensByAccountId(Long accountId);

    String generateJwtToken(UserDetails userDetails);

    String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
