package com.example.portfolio.webstorespring.IT.controllers.products;


import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static org.assertj.core.api.Assertions.assertThat;

class ProducerControllerIT extends AbstractBaseControllerIT<ProducerRequest, ProducerResponse, Producer> {

    @Autowired
    private ProducerRepository producerRepository;

    @Override
    protected void setup() {
        producerRepository.deleteAll();
        Producer savedProducer = producerRepository.save(createProducer());
        savedEntityId = savedProducer.getId();
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
        Optional<Producer> optionalProducer = producerRepository.findById(response.getId());
        assertThat(optionalProducer).isPresent();

        assertThat(response.getId()).isNotNull().isEqualTo(optionalProducer.get().getId());
        assertThat(response.getName()).isEqualTo(request.getName())
                .isEqualTo(optionalProducer.get().getName());
    }

    @Override
    public void assertsFieldsWhenUpdate(ProducerRequest request,
                                        ProducerResponse response,
                                        Producer entity) {
        assertThat(entity.getId()).isEqualTo(savedEntityId).isEqualTo(response.getId());
        assertThat(entity.getName()).isEqualTo(request.getName()).isEqualTo(response.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProducerRequest request, Producer entity) {
        assertThat(entity.getName()).isNotEqualTo(request.getName());
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
    void shouldUpdateProducer_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateProducer_forAuthenticatedAdmin_thenStatusForbidden() {
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
