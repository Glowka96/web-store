package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.model.dto.accounts.AuthenticationResponse;
import com.example.porfolio.webstorespring.model.dto.accounts.LoginRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.security.auth.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final AccountDetailsService accountDetailsService;
    private final AuthService authService;

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = accountDetailsService.loadUserByUsername(loginRequest.getEmail());
        Account account = findAccountByEmail(loginRequest.getEmail());
        String jwtToken = authService.generateAuthToken(userDetails);

        authService.revokeAllUserAuthTokens(account);
        authService.saveAccountAuthToken(account, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "account", "email", email));
    }
}
