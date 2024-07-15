package com.example.portfolio.webstorespring.IT.controllers.products;


import com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProducerControllerIT extends AbstractBaseControllerIT<ProducerRequest, ProducerResponse, Producer> {

    @Autowired
    private ProducerRepository producerRepository;

    @Override
    protected String getUri() {
        return "/producers";
    }

    @Override
    protected ProducerRequest createRequest() {
        return ProducerBuilderHelper.createProducerRequest("Test producer");
    }

    @Override
    protected Class<ProducerResponse> getResponseTypeClass() {
        return ProducerResponse.class;
    }

    @Override
    protected List<Producer> getAllEntities() {
        return producerRepository.findAll();
    }

    @Override
    protected Optional<Producer> getOptionalEntityById() {
        return producerRepository.findById(id);
    }

    @Override
    protected void setup() {
        producerRepository.deleteAll();
        Producer savedProducer = producerRepository.save(createProducer());
        id = savedProducer.getId();
    }

    @Test
    void shouldGetAllProducer_forAuthenticatedAdmin_thenStatusOK() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<List<ProducerResponse>> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + getUri(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void shouldSaveProducer_forAuthenticatedAdmin_thenStatusCreated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveProducer_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateProducer_forAuthenticatedAdmin_thenStatusAccepted() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }
    @Test
    void shouldNotUpdateProducer_forAuthenticatedAdmin_thenStatusForbidden() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProducer_forAuthenticatedAdmin_thenStatusNotContent() {
        shouldDeleteEntityForAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProducer_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotDeleteEntityForAuthenticatedUser_thenStatusForbidden();
    }
}
