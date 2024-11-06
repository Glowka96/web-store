package com.example.portfolio.webstorespring.controllers;

import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractBaseControllerIT<T, R, E> extends AbstractAuthControllerIT
        implements BaseControllerIT<T, R, E>, AssertsFieldsIT<T, R, E>, InitTestData {

    protected Long savedEntityId;

    protected void shouldGetAllEntities_forEverybody_thenStatusOk() {
        sendGetAllEntitiesRequest(getAllUri(), null);
    }

    protected void shouldGetAllEntities_forAdmin_thenStatusOK() {
        HttpEntity<T> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        sendGetAllEntitiesRequest(getAllAdminUri(), httpEntity);
    }

    private void sendGetAllEntitiesRequest(String allAdminUri, HttpEntity<T> httpEntity) {
        ResponseEntity<List<R>> response = restTemplate.exchange(
                allAdminUri,
                HttpMethod.GET,
                httpEntity,
                getListResponseTypeClass()
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertsFieldsWhenGetAll(response.getBody());
    }

    protected void shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated() {
        T request = createRequest();
        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<R> response = sendPostRequest(httpEntity);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertsFieldsWhenSave(request, response.getBody());
        assertEntitiesSize(2);
    }

    protected void shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = sendPostRequest(httpEntity);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        assertEntitiesSize(1);
    }

    protected void shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());
        Optional<E> optionalEBeforeUpdate = getOptionalEntityBySavedId();

        ResponseEntity<R> response = sendPutRequest(httpEntity);

        Optional<E> optionalEAfterUpdate = getOptionalEntityBySavedId();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(optionalEBeforeUpdate.isPresent());
        Assertions.assertTrue(optionalEAfterUpdate.isPresent());
        assertsFieldsWhenUpdate(request, response.getBody(), optionalEBeforeUpdate.get(), optionalEAfterUpdate.get());
    }

    protected void shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = sendPutRequest(httpEntity);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertNull(response.getBody());

        Optional<E> optionalE = getOptionalEntityBySavedId();
        Assertions.assertTrue(optionalE.isPresent());
        assertsFieldsWhenNotUpdate(request, optionalE.get());
    }

    protected void shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<Void> response = sendDeleteRequest(httpEntity);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());

        Optional<E> optionalE = getOptionalEntityBySavedId();
        Assertions.assertFalse(optionalE.isPresent());
    }

    protected void shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = sendDeleteRequest(httpEntity);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();

        Optional<E> optionalE = getOptionalEntityBySavedId();
        Assertions.assertTrue(optionalE.isPresent());
    }

    private ResponseEntity<R> sendPostRequest(HttpEntity<T> httpEntity) {
        return restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.POST,
                httpEntity,
                getResponseTypeClass()
        );
    }

    private ResponseEntity<R> sendPutRequest(HttpEntity<T> httpEntity) {
        return restTemplate.exchange(
                getAllAdminUri() + "/" + savedEntityId,
                HttpMethod.PUT,
                httpEntity,
                getResponseTypeClass()
        );
    }

    private ResponseEntity<Void> sendDeleteRequest(HttpEntity<?> httpEntity) {
        return restTemplate.exchange(
                getAllAdminUri() + "/" + savedEntityId,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );
    }

    @Override
    public void assertsFieldsWhenGetAll(List<R> responses) {
        assertThat(responses).hasSize(1);
    }

    @Override
    public ParameterizedTypeReference<List<R>> getListResponseTypeClass() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private String getAllUri() {
        return localhostUri + getUri();
    }

    private String getAllAdminUri() {
        return localhostAdminUri + getUri();
    }

    private void assertEntitiesSize(int expected) {
        List<E> entities = getAllEntities();
        Assertions.assertEquals(entities.size(), expected);
    }
}
