package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

public interface AuthService {
    void saveAccountAuthToken(Account account, String jwtToken);

    void revokeAllUserAuthTokens(Account account);

    String generateAuthToken(UserDetails userDetails);

    String generateAuthToken(HashMap<String, Object> extraClaims, UserDetails userDetails);
}
