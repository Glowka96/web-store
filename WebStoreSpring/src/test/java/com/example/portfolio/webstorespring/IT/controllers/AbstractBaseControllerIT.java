package com.example.portfolio.webstorespring.IT.controllers;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractBaseControllerIT<T, R, E> extends AbstractAuthControllerIT
        implements BaseControllerIT<T, R, E>, AssertsFieldsIT<T, R, E> {

    protected Long id;

    protected void shouldGetAllEntities_forEverybody_thenStatusOk() {
        ResponseEntity<List<R>> response = restTemplate.exchange(
                getAllUri(),
                HttpMethod.GET,
                null,
                getListResponseTypeClass()
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertsFieldsWhenGetAll(response.getBody());
    }

    protected void shouldGetAllEntities_forAdmin_thenStatusOK() {
        HttpEntity<T> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<List<R>> response = restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.GET,
                httpEntity,
                getListResponseTypeClass()
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertsFieldsWhenGetAll(response.getBody());
    }

    protected void shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated() {
        T request = createRequest();
        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.POST,
                httpEntity,
                getResponseTypeClass()
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertsFieldsWhenSave(request, response.getBody());
        assertEntitiesSize(2);
    }


    protected void shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.POST,
                httpEntity,
                getResponseTypeClass()
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        assertEntitiesSize(1);
    }

    protected void shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.PUT,
                httpEntity,
                getResponseTypeClass()
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isPresent();
        assertsFieldsWhenUpdate(request, response.getBody(), optionalE.get());
    }

    protected void shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.PUT,
                httpEntity,
                getResponseTypeClass()
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();

        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isPresent();
        assertsFieldsWhenNotUpdate(request, optionalE.get());
    }

    protected void shouldDeleteEntityForAuthenticatedAdmin_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertThat(response.getBody()).isNull();

        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isNotPresent();
    }

    protected void shouldNotDeleteEntityForAuthenticatedUser_thenStatusForbidden() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();

        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isPresent();
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

    protected String getAllUri() {
        return LOCALHOST_URI + getUri();
    }

    protected String getAllAdminUri() {
        return LOCALHOST_ADMIN_URI + getUri();
    }

    private void assertEntitiesSize(int expected) {
        List<E> entities = getAllEntities();
        assertThat(entities).hasSize(expected);
    }
}
