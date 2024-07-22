package com.example.portfolio.webstorespring.IT.controllers.accounts;

import com.example.portfolio.webstorespring.IT.controllers.AbstractIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.LoginRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AuthenticationResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import com.natpryce.makeiteasy.Maker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginControllerIT extends AbstractIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    private static final String PASSWORD_STRING = "Password123*";

    @Override
    public void initTestData() {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void shouldLoginAccount_whenAccountEnabled_thenStatusOK() {
        ResponseEntity<AuthenticationResponse> response =
                sendRequest(a(BASIC_ACCOUNT)
                        .but(with(PASSWORD, encoder.encode(PASSWORD_STRING))));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();

        Optional<AuthToken> authToken =
                tokenRepository.findByToken(response.getBody().token());
        assertThat(authToken).isPresent();
    }

    @Test
    void shouldNotLoginAccount_whenAccountNotEnabled_thenStatusUnauthorized() {
        ResponseEntity<AuthenticationResponse> response =
                sendRequest(a(BASIC_ACCOUNT)
                        .but(with(ENABLED, Boolean.FALSE))
                        .but(with(PASSWORD, encoder.encode(PASSWORD_STRING))));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    private ResponseEntity<AuthenticationResponse> sendRequest(Maker<Account> BASIC_ACCOUNT) {
        Account savedAccount = accountRepository.save(make(BASIC_ACCOUNT));

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(
                new LoginRequest(savedAccount.getEmail(), PASSWORD_STRING));

        return restTemplate.exchange(
                localhostUri + "/login",
                HttpMethod.POST,
                httpEntity,
                AuthenticationResponse.class
        );
    }
}
