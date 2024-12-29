package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountConfTokenRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.ENABLED;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountConfTokenBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountConfTokenRepository tokenRepository;
    private String registrationURI;
    private String registrationConfirmTokenURI;
    private static final String CONFIRMED_MESSAGE = "Account confirmed.";
    private static final String REGISTER_MESSAGE =
            "Verify your email address using the link in your email.";
    private static final String RESEND_TOKEN_MESSAGE =
            "Your token is expired. Verify your email address using the new token link in your email.";

    @BeforeEach
    public void initTestData() {
        registrationURI = localhostUri + "/registrations";
        registrationConfirmTokenURI = registrationURI + "/confirm?token=";
    }

    @AfterEach
    void deleteTestData() {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void shouldRegisterAccount_thenStatusCreated() {
        RegistrationRequest registrationRequest = createRegistrationRequest();
        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(registrationRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                registrationURI,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(requireNonNull(response.getBody())
                .containsValue(REGISTER_MESSAGE));

        Optional<Account> accountOptional = accountRepository.findByEmail(registrationRequest.email());
        assertTrue(accountOptional.isPresent());
        assertFalse(accountOptional.get().getEnabled());
    }

    @Test
    void shouldConfirmAccountToken_thenStatusOk() {
        Account savedAccount = accountRepository.save(
                make(a(BASIC_ACCOUNT)
                        .but(with(ENABLED, Boolean.FALSE)))
        );
        AccountConfToken savedAccountConfToken = tokenRepository.save(
                make(a(BASIC_CONFIRMATION_TOKEN)
                        .but(with(ACCOUNT, savedAccount))
                        .but(with(CREATED_AT, LocalDateTime.now()))
                        .but(withNull(CONFIRMED_AT))
                        .but(with(EXPIRED_AT, LocalDateTime.now().plusMinutes(15))))
        );

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                registrationConfirmTokenURI + savedAccountConfToken.getToken(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(requireNonNull(response.getBody()).containsValue(CONFIRMED_MESSAGE));

        Optional<Account> accountOptional = accountRepository.findByEmail(savedAccount.getEmail());
        assertTrue(accountOptional.isPresent());
        assertTrue(accountOptional.get().getEnabled());
    }

    @Test
    void shouldSendNewConfirmAccountToken_whenPreviousTokenIsExpired_thenStatusOk() {
        Account savedAccount = accountRepository.save(
                make(a(BASIC_ACCOUNT)
                        .but(with(ENABLED, Boolean.FALSE)))
        );
        AccountConfToken savedAccountConfToken = tokenRepository.save(
                make(a(BASIC_CONFIRMATION_TOKEN)
                        .but(with(ACCOUNT, savedAccount))
                        .but(with(CREATED_AT, LocalDateTime.now().minusHours(1)))
                        .but(withNull(CONFIRMED_AT))
                        .but(with(EXPIRED_AT, LocalDateTime.now().minusHours(1))))
        );

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                registrationConfirmTokenURI + savedAccountConfToken.getToken(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(requireNonNull(response.getBody()).containsValue(RESEND_TOKEN_MESSAGE));

        Optional<Account> accountOptional = accountRepository.findByEmail(savedAccount.getEmail());
        assertTrue(accountOptional.isPresent());
        assertFalse(accountOptional.get().getEnabled());
    }
}
