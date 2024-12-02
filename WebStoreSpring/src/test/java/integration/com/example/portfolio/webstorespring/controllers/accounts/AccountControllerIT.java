package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountRequest;
import static org.junit.jupiter.api.Assertions.*;

class AccountControllerIT extends AbstractAuthControllerIT {

    private static final String ACCOUNT_URI = "/accounts";

    @Test
    void shouldGetAccount_forAuthenticatedAccount_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                localhostUri + ACCOUNT_URI,
                HttpMethod.GET,
                httpEntity,
                AccountResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldUpdateAccount_forAuthenticatedAccount_thenStatusOK() {
        AccountRequest accountRequest = createAccountRequest(
                "New test name",
                "New test lastname"
        );

        HttpEntity<AccountRequest> httpEntity = new HttpEntity<>(accountRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                localhostUri + ACCOUNT_URI,
                HttpMethod.PUT,
                httpEntity,
                AccountResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        Optional<Account> accountOptional =
                accountRepository.findByEmail(Objects.requireNonNull(response.getBody()).email());
        assertTrue(accountOptional.isPresent());
        assertEquals(accountRequest.firstName(), accountOptional.get().getFirstName(), response.getBody().firstName());
        assertEquals(accountRequest.lastName(), accountOptional.get().getLastName(), response.getBody().lastName());
    }

    @Test
    void shouldUpdatePassword_forAuthenticatedAccount_thenStatusOk() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("Password123*", "NewPassword1*");
        HttpEntity<UpdatePasswordRequest> httpEntity = new HttpEntity<>(updatePasswordRequest, getHttpHeaderWithUserToken());

        String beforeUpdatePassword = getSavedUser().getPassword();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                localhostUri + ACCOUNT_URI + "/passwords",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        Optional<Account> account = accountRepository.findByEmail(getSavedUser().getEmail());
        String afterUpdatePassword = account.get().getPassword();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(passwordEncoder.matches(updatePasswordRequest.newPassword(), afterUpdatePassword));
        assertNotEquals(beforeUpdatePassword, afterUpdatePassword);
    }

    @Test
    void shouldDeleteAccount_forAuthenticatedAccount_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                localhostUri + ACCOUNT_URI,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        assertFalse(accountRepository.existsByEmail("user@test.pl"));
    }
}
