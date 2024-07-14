package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractBaseControllerIT<T, R, E> extends AbstractAuthControllerIT {

    protected Long id;

    protected abstract String getUri();

    protected abstract T createRequest();

    protected abstract Class<R> getResponseType();

    protected abstract List<E> getAllEntities();

    protected abstract Optional<E> getOptionalEntityById();

    protected void shouldGetAllEntities() {
        ResponseEntity<List<R>> response = restTemplate.exchange(
                getAllUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
    }

    protected void shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.POST,
                httpEntity,
                getResponseType()
        );

        Object responseId = getResultMethodFromResponse(response, "getId");
        Object responseName= getResultMethodFromResponse(response, "getName");
        Method entityGetNameMethod = request.getClass().getMethod("getName");

        Object requestName = entityGetNameMethod.invoke(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(responseId).isNotNull();
        assertThat(responseName).isEqualTo(requestName);
        assertEntitiesSize(2);
    }


    protected void shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden() {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri(),
                HttpMethod.POST,
                httpEntity,
                getResponseType()
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        assertEntitiesSize(1);
    }

    protected void shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.PUT,
                httpEntity,
                getResponseType()
        );

        Object responseId = getResultMethodFromResponse(response, "getId");
        Object responseName = getResultMethodFromResponse(response, "getName");

        Object requestName = getRequestName(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertThat(responseId).isEqualTo(id);
        assertThat(responseName).isEqualTo(requestName);

        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isPresent();
        Object entityName = getEntityName(optionalE.get());
        assertThat(entityName).isEqualTo(requestName).isEqualTo(responseName);
    }

    protected void shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        T request = createRequest();

        HttpEntity<T> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<R> response = restTemplate.exchange(
                getAllAdminUri() + "/" + id,
                HttpMethod.PUT,
                httpEntity,
                getResponseType()
        );

        Object requestName = getRequestName(request);


        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        Optional<E> optionalE = getOptionalEntityById();
        assertThat(optionalE).isPresent();
        Object entityName = getEntityName(optionalE.get());
        assertThat(entityName).isNotEqualTo(requestName);
    }

    protected void shouldDeleteEntityForAuthenticatedAdmin_thenStatusNotContent() {
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

    private static <T> Object getRequestName(T request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return request.getClass().getMethod("getName").invoke(request);
    }

    @NotNull
    private static <R> Object getResultMethodFromResponse(ResponseEntity<R> response, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Objects.requireNonNull(response.getBody()).getClass().getMethod(methodName).invoke(response.getBody());
    }

    @NotNull
    private static <E> Object getEntityName(E entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return entity.getClass().getMethod("getName").invoke(entity);
    }
}
