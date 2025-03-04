package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
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
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.createAccountConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RestoreEmailControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountConfTokenRepository accountConfTokenRepository;
    private String confirmRestoreEmailUrl;
    private Account account;

    @BeforeEach
    void initTestData() {
        account = accountRepository.save(make(a(BASIC_ACCOUNT)
                .but(with(EMAIL, "newEmail@test.pl"))
                .but(with(BACKUPEMAIL, "oldEmail@test.pl")))
        );

        AccountConfToken accountConfToken = accountConfTokenRepository.save(
                createAccountConfToken(
                        account,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now()))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().plusDays(7)))
                        )
                )
        );

        confirmRestoreEmailUrl = localhostUri + "/restore-emails/confirm?token=" + accountConfToken.getToken();
    }

    @AfterEach
    void deleteTestData() {
        accountRepository.deleteAll();
        accountConfTokenRepository.deleteAll();
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
