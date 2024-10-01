package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountControllerIT extends AbstractAuthControllerIT {
    @Test
    void shouldGetAccount_forAuthenticatedAccount_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                localhostUri + "/accounts",
                HttpMethod.GET,
                httpEntity,
                AccountResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldUpdateAccount_forAuthenticatedAccount_thenStatusAccepted() {
        AccountRequest accountRequest = AccountBuilderHelper.createAccountRequest(
                "New test name",
                "New test lastname",
                "newPassword123$"
        );

        HttpEntity<AccountRequest> httpEntity = new HttpEntity<>(accountRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                localhostUri + "/accounts",
                HttpMethod.PUT,
                httpEntity,
                AccountResponse.class
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        assertNotNull(response.getBody());
        Optional<Account> accountOptional =
                accountRepository.findByEmail(Objects.requireNonNull(response.getBody()).getEmail());
        assertTrue(accountOptional.isPresent());

        assertEquals(accountRequest.getFirstName(), accountOptional.get().getFirstName(), response.getBody().getFirstName());
        assertEquals(accountRequest.getLastName(), accountOptional.get().getLastName(), response.getBody().getLastName());
        assertTrue(passwordEncoder.matches(accountRequest.getPassword(), accountOptional.get().getPassword()));
    }

    @Test
    void shouldDeleteAccount_forAuthenticatedAccount_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                localhostUri + "/accounts",
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        assertFalse(accountRepository.existsByEmail("user@test.pl"));
    }
}
