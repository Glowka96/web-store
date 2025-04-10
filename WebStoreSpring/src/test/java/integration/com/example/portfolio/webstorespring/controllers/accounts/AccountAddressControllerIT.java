package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountAddressRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountAddressResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountAddressControllerIT extends AbstractAuthControllerIT {

    private static final String ADDRESS_URI = "/accounts/addresses";

    @Test
    void shouldGetAccountAddress_forAuthenticatedAccount_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<AccountAddressResponse> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.GET,
                httpEntity,
                AccountAddressResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getSavedAddress().getCity(), response.getBody().city());
        assertEquals(getSavedAddress().getPostcode(), response.getBody().postcode());
        assertEquals(getSavedAddress().getStreet(), response.getBody().street());
        assertEquals(getSavedAddress().getId(), response.getBody().id());
    }

    @Test
    void shouldSaveAccountAddress_forAuthenticatedAccount_thenStatusCreated() {
        addressRepository.deleteAll();
        assertTrue(addressRepository.findById(getSavedAddress().getId()).isEmpty());

        AccountAddressRequest addressRequest = createAccountAddressRequest();
        HttpEntity<AccountAddressRequest> httpEntity = new HttpEntity<>(addressRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountAddressResponse> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.POST,
                httpEntity,
                AccountAddressResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(addressRequest.city(), response.getBody().city());
        assertEquals(addressRequest.postcode(), response.getBody().postcode());
        assertEquals(getSavedUser().getId(), response.getBody().id());
        assertTrue(addressRepository.findById(getSavedUser().getId()).isPresent());
    }

    @Test
    void shouldUpdateAccountAddress_forAuthenticatedAccount_thenStatusOK() {
        AccountAddressRequest addressRequest = new AccountAddressRequest("Test", "99-000", "New-street 3");

        HttpEntity<AccountAddressRequest> httpEntity = new HttpEntity<>(addressRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountAddressResponse> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.PUT,
                httpEntity,
                AccountAddressResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().city()).isEqualTo(addressRequest.city())
                .isNotEqualTo(getSavedAddress().getCity());
        assertThat(response.getBody().postcode()).isEqualTo(addressRequest.postcode())
                .isNotEqualTo(getSavedAddress().getPostcode());
        assertThat(response.getBody().street()).isEqualTo(addressRequest.street())
                .isNotEqualTo(getSavedAddress().getStreet());
        assertEquals(getSavedAddress().getId(), response.getBody().id());
    }

    @Test
    void shouldDeleteAccountAddress_forAuthenticatedAccount_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(addressRepository.findById(getSavedUser().getId()).isEmpty());
    }
}
