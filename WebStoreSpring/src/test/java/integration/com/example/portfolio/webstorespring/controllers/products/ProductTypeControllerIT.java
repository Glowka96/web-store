package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductTypeResponse;
import com.example.portfolio.webstorespring.models.entities.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeControllerIT extends AbstractBaseControllerIT<ProductTypeRequest, ProductTypeResponse, ProductType> {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    @BeforeEach
    public void initTestData() {
        ProductType savedProductType = productTypeRepository.save(createProductType());
        savedEntityId = savedProductType.getId();
    }

    @Override
    @AfterEach
    public void deleteTestData() {
        productTypeRepository.deleteAll();
    }

    @Override
    public String getUri() {
        return "/product-types";
    }

    @Override
    public ProductTypeRequest createRequest() {
        return createProductTypeRequest("Test product type");
    }

    @Override
    public Class<ProductTypeResponse> getResponseTypeClass() {
        return ProductTypeResponse.class;
    }

    @Override
    public List<ProductType> getAllEntities() {
        return productTypeRepository.findAll();
    }

    @Override
    public Optional<ProductType> getOptionalEntityBySavedId() {
        return productTypeRepository.findById(savedEntityId);
    }

    @Override
    public void assertsFieldsWhenSave(ProductTypeRequest request,
                                      ProductTypeResponse response) {
        Optional<ProductType> optionalProductType =
                productTypeRepository.findById(response.id());
        assertTrue(optionalProductType.isPresent());

        assertThat(response.id()).isNotNull().isEqualTo(optionalProductType.get().getId());
        assertEquals(request.name(), optionalProductType.get().getName(), response.name());
    }

    @Override
    public void assertsFieldsWhenUpdate(ProductTypeRequest request,
                                        ProductTypeResponse response,
                                        ProductType entityBeforeUpdate,
                                        ProductType entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.id())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.name())
                .isEqualTo(response.name())
                .isNotEqualTo(entityBeforeUpdate.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProductTypeRequest request,
                                           ProductType entity) {
        assertNotEquals(request.name(), entity.getName());
    }

    @Test
    void shouldGetAllProductTypes_forEverybody_thenStatusOk() {
        shouldGetAllEntities_forEverybody_thenStatusOk();
    }

    @Test
    void shouldSaveProductType_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateProductType_forAuthenticatedAdmin_thenStatusOK() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK();
    }

    @Test
    void shouldNotUpdateProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProductType_forAuthenticatedAdmin_thenStatusNoContent(){
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
