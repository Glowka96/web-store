package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeRequest;
import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
class ProductTypeControllerIT extends AbstractBaseControllerIT<ProductTypeRequest, ProductTypeResponse, ProductType> {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    protected void setup() {
        productTypeRepository.deleteAll();

        ProductType savedProductType = productTypeRepository.save(createProductType());
        savedEntityId = savedProductType.getId();
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
                productTypeRepository.findById(response.getId());
        assertThat(optionalProductType).isPresent();

        assertThat(response.getId()).isNotNull().isEqualTo(optionalProductType.get().getId());
        assertThat(response.getName()).isEqualTo(request.getName())
                .isEqualTo(optionalProductType.get().getName());
    }

    @Override
    public void assertsFieldsWhenUpdate(ProductTypeRequest request,
                                        ProductTypeResponse response,
                                        ProductType entity) {
        assertThat(entity.getId()).isEqualTo(savedEntityId).isEqualTo(response.getId());
        assertThat(entity.getName()).isEqualTo(request.getName()).isEqualTo(response.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProductTypeRequest request,
                                           ProductType entity) {
        log.info("asserts not Update");
        assertThat(entity.getName()).isNotEqualTo(request.getName());
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
    void shouldUpdateProductType_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProductType_forAuthenticatedAdmin_thenStatusNoContent(){
        shouldDeleteEntityForAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProductType_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotDeleteEntityForAuthenticatedUser_thenStatusForbidden();
    }
}