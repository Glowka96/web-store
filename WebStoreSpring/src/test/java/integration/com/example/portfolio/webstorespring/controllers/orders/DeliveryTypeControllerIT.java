package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.models.entities.orders.DeliveryType;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryTypeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryTypeControllerIT extends AbstractBaseControllerIT<DeliveryTypeRequest, DeliveryTypeResponse, DeliveryType> {

    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;

    @Override
    @BeforeEach
    public void initTestData() {
        DeliveryType savedDeliveryType = deliveryTypeRepository.save(createDeliveryType());
        savedEntityId = savedDeliveryType.getId();
    }

    @Override
    @AfterEach
    public void deleteTestData() {
        deliveryTypeRepository.deleteAll();
    }

    @Override
    public void assertsFieldsWhenSave(DeliveryTypeRequest request,
                                      DeliveryTypeResponse response) {
        Optional<DeliveryType> optionalDeliveryType =
                deliveryTypeRepository.findById(response.id());
        assertTrue(optionalDeliveryType.isPresent());

        assertThat(response.id()).isNotNull().isEqualTo(optionalDeliveryType.get().getId());
        assertEquals(optionalDeliveryType.get().getName(), response.name());
        assertEquals(optionalDeliveryType.get().getPrice(), response.price());
    }

    @Override
    public void assertsFieldsWhenUpdate(DeliveryTypeRequest request,
                                        DeliveryTypeResponse response,
                                        DeliveryType entityBeforeUpdate,
                                        DeliveryType entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.id())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.name())
                .isEqualTo(response.name())
                .isNotEqualTo(entityBeforeUpdate.getName());
        assertThat(entityAfterUpdate.getPrice()).isEqualTo(request.price())
                .isEqualTo(response.price())
                .isNotEqualTo(entityBeforeUpdate.getPrice());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(DeliveryTypeRequest request,
                                           DeliveryType entity) {
        assertNotEquals(request.name(), entity.getName());
        assertNotEquals(request.price(), entity.getPrice());
    }

    @Override
    public String getUri() {
        return "/delivery-types";
    }

    @Override
    public DeliveryTypeRequest createRequest() {
        return createDeliveryTypeRequest("Test delivery", BigDecimal.valueOf(20L, 2));
    }

    @Override
    public Class<DeliveryTypeResponse> getResponseTypeClass() {
        return DeliveryTypeResponse.class;
    }

    @Override
    public List<DeliveryType> getAllEntities() {
        return deliveryTypeRepository.findAll();
    }

    @Override
    public Optional<DeliveryType> getOptionalEntityBySavedId() {
        return deliveryTypeRepository.findById(savedEntityId);
    }

    @Test
    void shouldGetAllDeliveryType_forEverybody_thenStatusOk() {
        shouldGetAllEntities_forEverybody_thenStatusOk();
    }

    @Test
    void shouldSaveDeliveryType_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSavedDeliveryType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateDeliveryType_forAuthenticatedAdmin_thenStatusOK() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK();
    }

    @Test
    void shouldNotUpdateDeliveryType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteDeliveryType_forAuthenticatedAdmin_thenStatusNoContent() {
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteDeliveryType_forAuthenticatedUser_thenStatusForbidden () {
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
