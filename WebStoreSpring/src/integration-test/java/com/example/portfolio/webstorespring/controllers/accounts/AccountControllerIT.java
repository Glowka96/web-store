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

import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(response.getBody()).isNotNull();
        AccountResponse accountResponse = response.getBody();
        Optional<Account> accountOptional =
                accountRepository.findByEmail(Objects.requireNonNull(accountResponse).getEmail());
        assertThat(accountOptional).isPresent();

        Account account = accountOptional.get();
        assertThat(account.getFirstName()).isEqualTo(accountRequest.getFirstName())
                .isEqualTo(accountResponse.getFirstName());
        assertThat(account.getLastName()).isEqualTo(accountRequest.getLastName())
                .isEqualTo(accountResponse.getLastName());
        assertTrue(passwordEncoder.matches(accountRequest.getPassword(), account.getPassword()));
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
        assertThat(response.getBody()).isNull();
        assertFalse(accountRepository.existsByEmail("user@test.pl"));
    }
}
