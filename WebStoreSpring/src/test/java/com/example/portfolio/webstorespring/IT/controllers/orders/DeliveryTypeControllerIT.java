package com.example.portfolio.webstorespring.IT.controllers.orders;

import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
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
                deliveryTypeRepository.findById(response.getId());
        assertThat(optionalDeliveryType).isPresent();

        assertThat(response.getId()).isNotNull().isEqualTo(optionalDeliveryType.get().getId());
        assertThat(response.getName()).isEqualTo(optionalDeliveryType.get().getName());
        assertThat(response.getPrice()).isEqualTo(optionalDeliveryType.get().getPrice());
    }

    @Override
    public void assertsFieldsWhenUpdate(DeliveryTypeRequest request,
                                        DeliveryTypeResponse response,
                                        DeliveryType entityBeforeUpdate,
                                        DeliveryType entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.getId())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.getName())
                .isEqualTo(response.getName())
                .isNotEqualTo(entityBeforeUpdate.getName());
        assertThat(entityAfterUpdate.getPrice()).isEqualTo(request.getPrice())
                .isEqualTo(response.getPrice())
                .isNotEqualTo(entityBeforeUpdate.getPrice());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(DeliveryTypeRequest request,
                                           DeliveryType entity) {
        assertThat(entity.getName()).isNotEqualTo(request.getName());
        assertThat(entity.getPrice()).isNotEqualTo(request.getPrice());
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
    void shouldUpdateDeliveryType_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
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
