package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.LoginRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AuthenticationResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import com.example.portfolio.webstorespring.services.authentications.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Transactional
    public AuthenticationResponse login(LoginRequest request) {
        log.info("Authenticating account");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        log.debug("Getting account from authentication principal.");
        UserDetails userDetails = (AccountDetails) authentication.getPrincipal();
        Account account = ((AccountDetails) authentication.getPrincipal()).getAccount();
        String jwtToken = authService.generateJwtToken(userDetails);

        authService.revokeAllAccountAuthTokensByAccountId(account.getId());
        authService.saveAccountAuthToken(account, jwtToken);
        log.info("Sending JWT token.");
        return new AuthenticationResponse(jwtToken);
    }
}
