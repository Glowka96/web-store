package com.example.portfolio.webstorespring.controllers.products;


import com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProducerRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProducerResponse;
import com.example.portfolio.webstorespring.models.entities.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProducerControllerIT extends AbstractBaseControllerIT<ProducerRequest, ProducerResponse, Producer> {

    @Autowired
    private ProducerRepository producerRepository;

    @Override
    @BeforeEach
    public void initTestData() {
        Producer savedProducer = producerRepository.save(ProducerBuilderHelper.createProducer());
        savedEntityId = savedProducer.getId();
    }

    @Override
    @AfterEach
    public void deleteTestData() {
        producerRepository.deleteAll();
    }

    @Override
    public String getUri() {
        return "/producers";
    }

    @Override
    public ProducerRequest createRequest() {
        return ProducerBuilderHelper.createProducerRequest("Test producer");
    }

    @Override
    public Class<ProducerResponse> getResponseTypeClass() {
        return ProducerResponse.class;
    }

    @Override
    public List<Producer> getAllEntities() {
        return producerRepository.findAll();
    }

    @Override
    public Optional<Producer> getOptionalEntityBySavedId() {
        return producerRepository.findById(savedEntityId);
    }

    @Override
    public void assertsFieldsWhenSave(ProducerRequest request,
                                      ProducerResponse response) {
        Optional<Producer> optionalProducer = producerRepository.findById(response.id());
        assertTrue(optionalProducer.isPresent());

        assertThat(response.id()).isNotNull().isEqualTo(optionalProducer.get().getId());
        assertEquals(request.name(), optionalProducer.get().getName(), response.name());
    }

    @Override
    public void assertsFieldsWhenUpdate(ProducerRequest request,
                                        ProducerResponse response,
                                        Producer entityBeforeUpdate,
                                        Producer entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.id())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.name())
                .isEqualTo(response.name())
                .isNotEqualTo(entityBeforeUpdate.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProducerRequest request, Producer entity) {
        assertNotEquals(request.name(), entity.getName());
    }

    @Test
    void shouldGetAllProducer_forAuthenticatedAdmin_thenStatusOK() {
        shouldGetAllEntities_forAdmin_thenStatusOK();
    }

    @Test
    void shouldSaveProducer_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveProducer_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateProducer_forAuthenticatedAdmin_thenStatusOK() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK();
    }

    @Test
    void shouldNotUpdateProducer_forAuthenticatedAdmin_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProducer_forAuthenticatedAdmin_thenStatusNotContent() {
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProducer_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
