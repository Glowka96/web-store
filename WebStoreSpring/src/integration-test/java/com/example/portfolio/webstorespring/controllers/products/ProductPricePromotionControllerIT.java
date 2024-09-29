package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductPricePromotionRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.createProductPricePromotionRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
class ProductPricePromotionControllerIT extends AbstractAuthControllerIT{

    @Autowired
    private ProductPricePromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    private final static String PRODUCTS_PROMOTIONS_URI = "/products/promotions";
    private Long savedProductId;
    private ProductPricePromotionRequest promotionRequest;

    @BeforeEach
    public void initTestData() {
        Product savedProduct = productRepository.save(make(a(BASIC_PRODUCT)
                .but(withNull(SUBCATEGORY))
                .but(withNull(PRODUCT_TYPE))
                .but(withNull(PRODUCER))
                .but(withNull(PRICE_PROMOTIONS)))
        );

        promotionRequest = createProductPricePromotionRequest();
        savedProductId = savedProduct.getId();
        promotionRequest.setProductId(savedProductId);
        promotionRequest.setEndDate(LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.SECONDS));
    }

    @AfterEach
    public void deleteTestData() {
        promotionRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldSavePromotion_forAuthenticatedAdmin_thenStatusCreated() {
        HttpEntity<ProductPricePromotionRequest> httpEntity =
                new HttpEntity<>(promotionRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<ProductPricePromotionResponse> response = restTemplate.exchange(
                localhostAdminUri + PRODUCTS_PROMOTIONS_URI,
                HttpMethod.POST,
                httpEntity,
                ProductPricePromotionResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ProductPricePromotionResponse productPricePromotionResponse = response.getBody();

        assertThat(productPricePromotionResponse).isNotNull();
        assertThat(productPricePromotionResponse.getPromotionPrice()).isEqualTo(promotionRequest.getPromotionPrice());
        assertThat(productPricePromotionResponse.getStartDate()).isEqualTo(promotionRequest.getStartDate());
        assertThat(productPricePromotionResponse.getEndDate()).isEqualTo(promotionRequest.getEndDate());
        assertThat(productPricePromotionResponse.getProductResponse().getId())
                .isEqualTo(savedProductId).isEqualTo(promotionRequest.getProductId());
    }

    @Test
    void shouldNotSavePromotion_forAuthenticatedUser_thenStatusForbidden() {
        HttpEntity<ProductPricePromotionRequest> httpEntity =
                new HttpEntity<>(promotionRequest, getHttpHeaderWithUserToken());

        ResponseEntity<ProductPricePromotionResponse> response = restTemplate.exchange(
                localhostAdminUri + PRODUCTS_PROMOTIONS_URI,
                HttpMethod.POST,
                httpEntity,
                ProductPricePromotionResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
    }
}
