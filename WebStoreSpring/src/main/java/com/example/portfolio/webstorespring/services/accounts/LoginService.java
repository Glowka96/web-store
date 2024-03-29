package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.LoginRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AuthenticationResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.authentication.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        UserDetails userDetails = (AccountDetails) authentication.getPrincipal();
        Account account = ((AccountDetails) authentication.getPrincipal()).getAccount();
        String jwtToken = authService.generateJwtToken(userDetails);

        authService.revokeAllAccountAuthTokensByAccountId(account.getId());
        authService.saveAccountAuthToken(account, jwtToken);

        return new AuthenticationResponse(jwtToken);
    }
}
