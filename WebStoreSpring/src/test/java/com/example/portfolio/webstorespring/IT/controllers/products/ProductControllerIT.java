package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.IT.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductControllerIT extends AbstractBaseControllerIT<ProductRequest, ProductResponse, Product> {

    @Autowired
    private InitProductTestData initProductTestData;
    @Autowired
    private ProductRepository productRepository;
    private static final String URI_PRODUCT = "/products";
    private String uri;
    private Long subcategoryId;
    private Long producerId;

    @BeforeEach
    @Override
    public void initTestData() {
        initProductTestData.initOneProduct();
        savedEntityId = initProductTestData.getProductIdThatHasNoPromotion();
        subcategoryId = initProductTestData.getSubId();
        producerId = initProductTestData.getProducerId();

        uri = "/subcategories/" + subcategoryId + " /producers/" + producerId + "/products";
    }

    @AfterEach
    public void deleteTestData() {
        initProductTestData.deleteTestData();
    }

    @Override
    public void assertsFieldsWhenSave(ProductRequest request, ProductResponse response) {
        Optional<Product> optionalProduct = productRepository.findProductByIdWithPromotion(
                response.getId()
        );
        assertThat(optionalProduct).isPresent();

        assertThat(response.getId()).isNotNull().isEqualTo(optionalProduct.get().getId());
        assertThat(response.getName()).isEqualTo(optionalProduct.get().getName())
                .isEqualTo(request.getName());
        assertThat(response.getDescription()).isEqualTo(optionalProduct.get().getDescription())
                .isEqualTo(request.getDescription());
        assertThat(response.getPrice()).isEqualByComparingTo(optionalProduct.get().getPrice())
                .isEqualByComparingTo(request.getPrice());
        assertThat(response.getQuantity()).isEqualTo(optionalProduct.get().getQuantity())
                .isEqualTo(request.getQuantity());
        assertThat(response.getImageUrl()).isEqualTo(optionalProduct.get().getImageUrl())
                .isEqualTo(request.getImageUrl());
        assertThat(response.getProductTypeResponse().getId())
                .isEqualTo(optionalProduct.get().getType().getId())
                .isEqualTo(request.getProductTypeId());
        assertThat(response.getProducerResponse().getId())
                .isEqualTo(optionalProduct.get().getProducer().getId())
                .isEqualTo(producerId);
        assertThat(optionalProduct.get().getSubcategory().getId()).isEqualTo(subcategoryId);
    }

    @Override
    public void assertsFieldsWhenUpdate(ProductRequest request,
                                        ProductResponse response,
                                        Product entityBeforeUpdate,
                                        Product entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.getId())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.getName())
                .isEqualTo(response.getName())
                .isNotEqualTo(entityBeforeUpdate.getName());
        assertThat(entityAfterUpdate.getDescription()).isEqualTo(request.getDescription())
                .isEqualTo(response.getDescription())
                .isNotEqualTo(entityBeforeUpdate.getDescription());
        assertThat(entityAfterUpdate.getPrice()).isEqualByComparingTo(request.getPrice())
                .isEqualByComparingTo(response.getPrice())
                .isNotEqualByComparingTo(entityBeforeUpdate.getPrice());
        assertThat(entityAfterUpdate.getQuantity()).isEqualTo(request.getQuantity())
                .isEqualTo(response.getQuantity())
                .isNotEqualTo(entityBeforeUpdate.getQuantity());
        assertThat(entityAfterUpdate.getImageUrl()).isEqualTo(request.getImageUrl())
                .isEqualTo(response.getImageUrl())
                .isNotEqualTo(entityBeforeUpdate.getImageUrl());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProductRequest request, Product entity) {
        assertThat(entity.getName()).isNotEqualTo(request.getName());
        assertThat(entity.getDescription()).isNotEqualTo(request.getDescription());
        assertThat(entity.getPrice()).isNotEqualByComparingTo(request.getPrice());
        assertThat(entity.getQuantity()).isNotEqualTo(request.getQuantity());
        assertThat(entity.getImageUrl()).isNotEqualTo(request.getImageUrl());
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public ProductRequest createRequest() {
        return ProductBuilderHelper.createProductRequest(
                "New name",
                "New description",
                BigDecimal.valueOf(30.00),
                "https://newimagetest.pl/image.jpg",
                20L,
                initProductTestData.getProductTypeId()
        );
    }

    @Override
    public Class<ProductResponse> getResponseTypeClass() {
        return ProductResponse.class;
    }

    @Override
    public List<Product> getAllEntities() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getOptionalEntityBySavedId() {
        return productRepository.findById(savedEntityId);
    }

    @Test
    void shouldGetAllProduct_forAuthenticatedAdmin_thenStatusOk() {
        uri = URI_PRODUCT;
        shouldGetAllEntities_forAdmin_thenStatusOK();
    }

    @Test
    void shouldGetProductById_forEverybody_thenStatusOk() {
        ResponseEntity<ProductWithProducerAndPromotionDTO> response = restTemplate.exchange(
                localhostUri + "/products/" + savedEntityId,
                HttpMethod.GET,
                null,
                ProductWithProducerAndPromotionDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().id()).isEqualTo(savedEntityId);
    }

    @Test
    void shouldSaveProduct_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveProduct_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateProduct_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateProduct_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteProduct_forAuthenticatedAdmin_thenStatusNoContent() {
        uri = URI_PRODUCT;
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteProduct_forAuthenticatedUser_thenStatusForbidden() {
        uri = URI_PRODUCT;
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
