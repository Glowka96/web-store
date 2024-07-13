package com.example.portfolio.webstorespring.IT.controllers.products;


import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducerRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProducerControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private ProducerRepository producerRepository;

    private static final String PRODUCER_URI = "/producers";
    private Long producerId;

    @BeforeEach
    @Override
    public void init() {
        producerRepository.deleteAll();
        Producer savedProducer = producerRepository.save(createProducer());
        producerId = savedProducer.getId();
        super.init();
    }

    @Test
    void shouldGetAllProducer_forAuthenticatedAdmin_thenStatusOK() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<List<ProducerResponse>> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void shouldNotGetAllProducer_forAuthenticatedUser_thenStatusForbidden() {
        ResponseEntity<List<ProducerResponse>> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldSaveProducer_forAuthenticatedAdmin_thenStatusCreated() {
        ProducerRequest producerRequest = createProducerRequest();

        HttpEntity<ProducerRequest> httpEntity = new HttpEntity<>(producerRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<ProducerResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI,
                HttpMethod.POST,
                httpEntity,
                ProducerResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(producerRequest.getName());
        List<Producer> producers = producerRepository.findAll();
        assertThat(producers).hasSize(2);
    }

    @Test
    void shouldNotSaveProducer_forAuthenticatedUser_thenStatusForbidden() {
        ProducerRequest producerRequest = createProducerRequest();

        HttpEntity<ProducerRequest> httpEntity = new HttpEntity<>(producerRequest, getHttpHeaderWithUserToken());

        ResponseEntity<ProducerResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI,
                HttpMethod.POST,
                httpEntity,
                ProducerResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        List<Producer> producers = producerRepository.findAll();
        assertThat(producers).hasSize(1);
    }


    @Test
    void shouldUpdateProducer_forAuthenticatedAdmin_thenStatusAccepted() {
        ProducerRequest producerRequest = createProducerRequest("Update name");

        HttpEntity<ProducerRequest> httpEntity = new HttpEntity<>(producerRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<ProducerResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI + "/" + producerId,
                HttpMethod.PUT,
                httpEntity,
                ProducerResponse.class
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(producerId);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(producerRequest.getName());
        Optional<Producer> foundProducer = producerRepository.findById(producerId);
        assertThat(foundProducer.get().getName()).isEqualTo(producerRequest.getName());
    }

    @Test
    void shouldNotUpdateProducer_forAuthenticatedUser_thenStatusForbidden() {
        ProducerRequest producerRequest = createProducerRequest("Update name");

        HttpEntity<ProducerRequest> httpEntity = new HttpEntity<>(producerRequest, getHttpHeaderWithUserToken());

        ResponseEntity<ProducerResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI + "/" + producerId,
                HttpMethod.PUT,
                httpEntity,
                ProducerResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        Optional<Producer> foundProducer = producerRepository.findById(producerId);
        assertThat(foundProducer.get().getName()).isNotEqualTo(producerRequest.getName());
    }

    @Test
    void shouldDeleteProducer_forAuthenticatedAdmin_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI + "/" + producerId,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        Optional<Producer> foundProducer = producerRepository.findById(producerId);
        assertThat(foundProducer).isNotPresent();
    }

    @Test
    void shouldNotDeleteProducer_forAuthenticatedUser_thenStatusForbidden() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + PRODUCER_URI + "/" + producerId,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
        Optional<Producer> foundProducer = producerRepository.findById(producerId);
        assertThat(foundProducer).isPresent();
    }
}
