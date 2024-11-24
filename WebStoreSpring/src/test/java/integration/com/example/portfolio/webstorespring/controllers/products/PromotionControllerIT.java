package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.PromotionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PromotionControllerIT extends AbstractAuthControllerIT{

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final String PRODUCTS_PROMOTIONS_URI = "/products/promotions";
    private Long savedProductId;
    private PromotionRequest promotionRequest;

    @BeforeEach
    public void initTestData() {
        Product savedProduct = productRepository.save(make(a(BASIC_PRODUCT)
                .but(withNull(SUBCATEGORY))
                .but(withNull(PRODUCT_TYPE))
                .but(withNull(PRODUCER))
                .but(withNull(PRICE_PROMOTIONS)))
        );

        savedProductId = savedProduct.getId();
        promotionRequest = new PromotionRequest(
                savedProductId,
                BigDecimal.valueOf(10.0),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(15).truncatedTo(ChronoUnit.SECONDS)
        );
    }

    @AfterEach
    public void deleteTestData() {
        promotionRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldSavePromotion_forAuthenticatedAdmin_thenStatusCreated() {
        HttpEntity<PromotionRequest> httpEntity =
                new HttpEntity<>(promotionRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<PromotionResponse> response = restTemplate.exchange(
                localhostAdminUri + PRODUCTS_PROMOTIONS_URI,
                HttpMethod.POST,
                httpEntity,
                PromotionResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PromotionResponse promotionResponse = response.getBody();

        assertNotNull(promotionResponse);
        assertEquals(promotionRequest.promotionPrice(), promotionResponse.promotionPrice());
        assertEquals(promotionRequest.startDate(), promotionResponse.startDate());
        assertEquals(promotionRequest.endDate(), promotionResponse.endDate());
        assertEquals(savedProductId, promotionRequest.productId(), promotionResponse.productResponse().id());
    }

    @Test
    void shouldNotSavePromotion_forAuthenticatedUser_thenStatusForbidden() {
        HttpEntity<PromotionRequest> httpEntity =
                new HttpEntity<>(promotionRequest, getHttpHeaderWithUserToken());

        ResponseEntity<PromotionResponse> response = restTemplate.exchange(
                localhostAdminUri + PRODUCTS_PROMOTIONS_URI,
                HttpMethod.POST,
                httpEntity,
                PromotionResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertThat(response.getBody()).isNull();
    }
}
