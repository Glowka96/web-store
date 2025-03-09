package com.example.portfolio.webstorespring.services.authentications;

import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthService {
    void saveAccountAuthToken(Account account, String jwtToken);

    void revokeAllAccountAuthTokensByAccountId(Long accountId);

    String generateJwtToken(UserDetails userDetails);

    String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
