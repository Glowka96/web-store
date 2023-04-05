package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {
    void saveAccountToken(Account account, String jwtToken);
    void revokeAllUserTokens(Account account);
}
