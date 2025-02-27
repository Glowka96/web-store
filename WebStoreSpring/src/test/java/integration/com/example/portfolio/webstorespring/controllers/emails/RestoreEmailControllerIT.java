package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RestoreEmailControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    private String confirmRestoreEmailUrl;
    private Account account;

    @BeforeEach
    void initTestData() {
        account = accountRepository.save(make(a(BASIC_ACCOUNT)
                .but(with(EMAIL, "newEmail@test.pl"))
                .but(with(BACKUPEMAIL, "oldEmail@test.pl")))
        );

        ConfirmationToken confirmationToken = confirmationTokenRepository.save(make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(with(CREATED_AT, LocalDateTime.now()))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRED_AT, LocalDateTime.now().plusDays(7))))
        );
        confirmRestoreEmailUrl = localhostUri + "/restore-email/confirm?token=" + confirmationToken.getToken();
    }

    @AfterEach
    void deleteTestData() {
        accountRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
    }

    @Test
    void shouldConfirmRestoreEmail_forEveryone_thenStatusOK() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                confirmRestoreEmailUrl,
                HttpMethod.PATCH,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Old account email restored", Objects.requireNonNull(response.getBody()).get("message"));
        account = accountRepository.findById(account.getId()).get();
        assertEquals("oldEmail@test.pl", account.getEmail());
        assertNull(account.getBackupEmail());
    }
}
