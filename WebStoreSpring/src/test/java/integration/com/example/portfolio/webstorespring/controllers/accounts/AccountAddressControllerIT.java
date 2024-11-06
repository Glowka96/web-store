package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
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
        assertEquals(getSavedAddress().getCity(), response.getBody().getCity());
        assertEquals(getSavedAddress().getPostcode(), response.getBody().getPostcode());
        assertEquals(getSavedAddress().getStreet(), response.getBody().getStreet());
        assertEquals(getSavedAddress().getId(), response.getBody().getId());
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
        assertEquals(addressRequest.getCity(), response.getBody().getCity());
        assertEquals(addressRequest.getPostcode(), response.getBody().getPostcode());
        assertEquals(addressRequest.getStreet(), response.getBody().getStreet());
        assertEquals(getSavedUser().getId(), response.getBody().getId());
        assertTrue(addressRepository.findById(getSavedUser().getId()).isPresent());
    }

    @Test
    void shouldUpdateAccountAddress_forAuthenticatedAccount_thenStatusOK() {
        AccountAddressRequest addressRequest = createAccountAddressRequest(
                "Newnamecity",
                "00-000",
                "new-street 99"
        );

        HttpEntity<AccountAddressRequest> httpEntity = new HttpEntity<>(addressRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountAddressResponse> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.PUT,
                httpEntity,
                AccountAddressResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().getCity()).isEqualTo(addressRequest.getCity())
                .isNotEqualTo(getSavedAddress().getCity());
        assertThat(response.getBody().getPostcode()).isEqualTo(addressRequest.getPostcode())
                .isNotEqualTo(getSavedAddress().getPostcode());
        assertThat(response.getBody().getStreet()).isEqualTo(addressRequest.getStreet())
                .isNotEqualTo(getSavedAddress().getStreet());
        assertEquals(getSavedAddress().getId(), response.getBody().getId());
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
