package com.example.portfolio.webstorespring.IT.controllers.accounts;

import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCity()).isEqualTo(getSavedUserAccountAddress().getCity());
        assertThat(response.getBody().getPostcode()).isEqualTo(getSavedUserAccountAddress().getPostcode());
        assertThat(response.getBody().getStreet()).isEqualTo(getSavedUserAccountAddress().getStreet());
    }

    @Test
    void shouldSaveAccountAddress_forAuthenticatedAccount_thenStatusCreated() {
        addressRepository.deleteAll();
        assertTrue(addressRepository.findById(getSavedUserAccount().getId()).isEmpty());

        AccountAddressRequest addressRequest = createAccountAddressRequest();
        HttpEntity<AccountAddressRequest> httpEntity = new HttpEntity<>(addressRequest, getHttpHeaderWithUserToken());

        ResponseEntity<AccountAddressResponse> response = restTemplate.exchange(
                localhostUri + ADDRESS_URI,
                HttpMethod.POST,
                httpEntity,
                AccountAddressResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCity()).isEqualTo(addressRequest.getCity());
        assertThat(response.getBody().getPostcode()).isEqualTo(addressRequest.getPostcode());
        assertThat(response.getBody().getStreet()).isEqualTo(addressRequest.getStreet());
        assertThat(response.getBody().getId()).isEqualTo(getSavedUserAccount().getId());
        assertTrue(addressRepository.findById(getSavedUserAccount().getId()).isPresent());
    }

    @Test
    void shouldUpdateAccountAddress_forAuthenticatedAccount_thenStatusAccepted() {
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

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCity()).isEqualTo(addressRequest.getCity())
                .isNotEqualTo(getSavedUserAccountAddress().getCity());
        assertThat(response.getBody().getPostcode()).isEqualTo(addressRequest.getPostcode())
                .isNotEqualTo(getSavedUserAccountAddress().getPostcode());
        assertThat(response.getBody().getStreet()).isEqualTo(addressRequest.getStreet())
                .isNotEqualTo(getSavedUserAccountAddress().getStreet());
        assertThat(response.getBody().getId()).isEqualTo(getSavedUserAccountAddress().getId());
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
        assertTrue(addressRepository.findById(getSavedUserAccount().getId()).isEmpty());
    }
}
