package com.example.portfolio.webstorespring.IT.controllers.accounts;

import com.example.portfolio.webstorespring.IT.controllers.AbstractIT;
import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
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
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerIT extends AbstractIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @BeforeEach
    public void initTestData() {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void shouldRegisterAccount_thenStatusCreated() {
        RegistrationRequest registrationRequest = createRegistrationRequest();
        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(registrationRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                localhostUri + "/registration",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody())
                .containsValue("Verify email by the link sent on your email address"));

        Optional<Account> accountOptional = accountRepository.findByEmail(registrationRequest.getEmail());
        assertThat(accountOptional).isPresent();
        assertFalse(accountOptional.get().getEnabled());
    }

    @Test
    void shouldConfirmAccountToken_thenStatusOk() {
        Account savedAccount = accountRepository.save(make(a(AccountBuilderHelper.BASIC_ACCOUNT)));
        ConfirmationToken savedConfirmationToken = tokenRepository.save(
                make(a(BASIC_CONFIRMATION_TOKEN)
                        .but(with(ACCOUNT, savedAccount))
                        .but(with(CREATED_AT, LocalDateTime.now()))
                        .but(withNull(CONFIRMED_AT))
                        .but(with(EXPIRED_AT, LocalDateTime.now().plusMinutes(15))))
        );

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                localhostUri + "/registration/confirm?token=" + savedConfirmationToken.getToken(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody()).containsValue("Account confirmed"));

        Optional<Account> accountOptional = accountRepository.findByEmail(savedAccount.getEmail());
        assertThat(accountOptional).isPresent();
        assertTrue(accountOptional.get().getEnabled());
    }
}
