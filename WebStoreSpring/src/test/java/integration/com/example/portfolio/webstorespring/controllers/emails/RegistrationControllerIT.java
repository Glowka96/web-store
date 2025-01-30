package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.model.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
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
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.createAccountConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
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
            "Your token is expired. There is new confirmation link in your email.";

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
                createAccountConfToken(
                        savedAccount,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now()))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().plusMinutes(15)))
                        )
                )
        );

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                registrationConfirmTokenURI + savedAccountConfToken.getToken(),
                HttpMethod.GET,
                null,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertEquals(CONFIRMED_MESSAGE, requireNonNull(response.getBody().message()));

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
                createAccountConfToken(
                        savedAccount,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now().minusHours(1)))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().minusHours(1)))
                        )
                )
        );

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                registrationConfirmTokenURI + savedAccountConfToken.getToken(),
                HttpMethod.GET,
                null,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RESEND_TOKEN_MESSAGE, requireNonNull(response.getBody().message()));

        Optional<Account> accountOptional = accountRepository.findByEmail(savedAccount.getEmail());
        assertTrue(accountOptional.isPresent());
        assertFalse(accountOptional.get().getEnabled());
    }
}
