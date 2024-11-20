package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
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
import static org.junit.jupiter.api.Assertions.*;

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
        Optional<Product> optionalProduct = productRepository.findWithPromotionById(
                response.getId()
        );
        assertTrue(optionalProduct.isPresent());

        assertThat(response.getId()).isNotNull().isEqualTo(optionalProduct.get().getId());
        assertEquals(request.getName(), optionalProduct.get().getName(), response.getName());
        assertEquals(request.getDescription(), optionalProduct.get().getDescription(), response.getDescription());
        assertThat(response.getPrice()).isEqualByComparingTo(optionalProduct.get().getPrice())
                .isEqualByComparingTo(request.getPrice());
        assertEquals(request.getQuantity(), optionalProduct.get().getQuantity(), response.getQuantity());
        assertEquals(request.getImageUrl(), optionalProduct.get().getImageUrl(), response.getImageUrl());
        assertEquals(request.getProductTypeId(), optionalProduct.get().getType().getId(), response.getProductTypeResponse().id());
        assertEquals(producerId, optionalProduct.get().getProducer().getId(), response.getProducerResponse().id());
        assertEquals(subcategoryId, optionalProduct.get().getSubcategory().getId());
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
        assertNotEquals(request.getName(), entity.getName());
        assertNotEquals(request.getDescription(), entity.getDescription());
        assertNotEquals(request.getPrice(), entity.getPrice());
        assertNotEquals(request.getQuantity(), entity.getQuantity());
        assertNotEquals(request.getImageUrl(), entity.getImageUrl());
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
        assertNotNull(response.getBody());
        assertEquals(savedEntityId, response.getBody().id());
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
    void shouldUpdateProduct_forAuthenticatedAdmin_thenStatusOK() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK();
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
