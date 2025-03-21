package com.example.portfolio.webstorespring.controllers.emails;


import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.ResetPasswordRequest;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.createAccountConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ResetPasswordControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountConfTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    private String resetPasswordUri;
    private Account savedAccount;

    @BeforeEach
    void initTestData() {
        resetPasswordUri = localhostUri + "/reset-passwords";
        savedAccount = accountRepository.save(make(a(AccountBuilderHelper.BASIC_ACCOUNT)));
    }

    @AfterEach
    void deleteTestData() {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void shouldSendResetPassword_Link_forEverybody_thenStatusOk() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                resetPasswordUri + "?email=" + savedAccount.getEmail(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(Objects.requireNonNull(response.getBody())
                .containsValue("Sent reset password link to your email"));
    }

    @Test
    void shouldConfirmSendResetPassword_Link_withValidToken_thenStatusOK() {
        AccountConfToken savedAccountConfToken = tokenRepository.save(
                createAccountConfToken(
                        savedAccount,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now()))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().plusMinutes(15)))
                        )
                )
        );
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("newPassword123*");

        HttpEntity<ResetPasswordRequest> requestEntity = new HttpEntity<>(resetPasswordRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                resetPasswordUri + "/confirm?token=" + savedAccountConfToken.getToken(),
                HttpMethod.PATCH,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(Objects.requireNonNull(response.getBody())
                .containsValue("Your new password has been saved"));

        Optional<Account> optionalAccount = accountRepository.findByEmail(savedAccount.getEmail());
        assertTrue(optionalAccount.isPresent());
        assertTrue(encoder.matches("newPassword123*", optionalAccount.get().getPassword()));
    }
}
