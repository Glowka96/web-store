package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductQualityRequest;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.InitSubscriptionData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerIT extends AbstractBaseControllerIT<ProductRequest, ProductResponse, Product> {

    @Autowired
    private InitProductTestData initProductTestData;
    @Autowired
    private InitSubscriptionData initSubscriptionData;
    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;
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
                response.id()
        );
        assertTrue(optionalProduct.isPresent());

        assertThat(response.id()).isNotNull().isEqualTo(optionalProduct.get().getId());
        assertEquals(request.name(), optionalProduct.get().getName(), response.name());
        assertEquals(request.description(), optionalProduct.get().getDescription(), response.description());
        assertThat(response.price()).isEqualByComparingTo(optionalProduct.get().getPrice())
                .isEqualByComparingTo(request.price());
        assertEquals(request.quantity(), optionalProduct.get().getQuantity(), response.quantity());
        assertEquals(request.imageUrl(), optionalProduct.get().getImageUrl(), response.imageUrl());
        assertEquals(request.productTypeId(), optionalProduct.get().getType().getId(), response.productTypeResponse().id());
        assertEquals(producerId, optionalProduct.get().getProducer().getId(), response.producerResponse().id());
        assertEquals(subcategoryId, optionalProduct.get().getSubcategory().getId());
    }

    @Override
    public void assertsFieldsWhenUpdate(ProductRequest request,
                                        ProductResponse response,
                                        Product entityBeforeUpdate,
                                        Product entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.id())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.name())
                .isEqualTo(response.name())
                .isNotEqualTo(entityBeforeUpdate.getName());
        assertThat(entityAfterUpdate.getDescription()).isEqualTo(request.description())
                .isEqualTo(response.description())
                .isNotEqualTo(entityBeforeUpdate.getDescription());
        assertThat(entityAfterUpdate.getPrice()).isEqualByComparingTo(request.price())
                .isEqualByComparingTo(response.price())
                .isNotEqualByComparingTo(entityBeforeUpdate.getPrice());
        assertThat(entityAfterUpdate.getQuantity()).isEqualTo(request.quantity())
                .isEqualTo(response.quantity())
                .isNotEqualTo(entityBeforeUpdate.getQuantity());
        assertThat(entityAfterUpdate.getImageUrl()).isEqualTo(request.imageUrl())
                .isEqualTo(response.imageUrl())
                .isNotEqualTo(entityBeforeUpdate.getImageUrl());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(ProductRequest request, Product entity) {
        assertNotEquals(request.name(), entity.getName());
        assertNotEquals(request.description(), entity.getDescription());
        assertNotEquals(request.price(), entity.getPrice());
        assertNotEquals(request.quantity(), entity.getQuantity());
        assertNotEquals(request.imageUrl(), entity.getImageUrl());
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
    void shouldUpdateProductQuantityAndSendEmail_whenOldQuantityProductIsZero_forAuthenticationAdmin_thenStatusOK() {
        initSubscriptionData.init();
        ProductQualityRequest request = new ProductQualityRequest(initSubscriptionData.getProductId(), 100L);
        HttpEntity<ProductQualityRequest> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostAdminUri + "/products",
                HttpMethod.PATCH,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertNotNull(response);

        Optional<Product> productAfterUpdated = productRepository.findById(initSubscriptionData.getProductId());
        assertEquals(100L, productAfterUpdated.get().getQuantity());
        initSubscriptionData.deleteTestData();
    }

    @Test
    void shouldUpdateProductQuantity_forAuthenticationAdmin_thenStatusOK() {
        testOfUpdateProductQuantity(getHttpHeadersWithAdminToken(), HttpStatus.OK);

        Optional<Product> productAfterUpdated = getOptionalEntityBySavedId();
        assertEquals(100L, productAfterUpdated.get().getQuantity());
    }

    @Test
    void shouldNotUpdateProductQuantity_forAuthenticateUser_thenStatusForbidden() {
        testOfUpdateProductQuantity(getHttpHeaderWithUserToken(), HttpStatus.FORBIDDEN);

        Optional<Product> productAfterUpdated = getOptionalEntityBySavedId();
        assertNotEquals(100L, productAfterUpdated.get().getQuantity());
    }

    private void testOfUpdateProductQuantity(HttpHeaders httpHeaders, HttpStatus httpStatus) {
        ProductQualityRequest request = new ProductQualityRequest(savedEntityId, 100L);
        HttpEntity<ProductQualityRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostAdminUri + "/products",
                HttpMethod.PATCH,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertNotNull(response);
        assertEquals(httpStatus, response.getStatusCode());
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
