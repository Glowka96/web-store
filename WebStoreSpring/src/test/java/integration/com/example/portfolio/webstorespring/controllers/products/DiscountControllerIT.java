package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.DiscountBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.models.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.DiscountAdminResponse;
import com.example.portfolio.webstorespring.models.dto.products.response.DiscountUserResponse;
import com.example.portfolio.webstorespring.models.entity.products.Discount;
import com.example.portfolio.webstorespring.models.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DiscountControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    private static final String DISCOUNT_URI = "/discounts";
    private Discount discount;

    @BeforeEach
    void initTestData() {
        Subcategory subcategory = subcategoryRepository.save(SubcategoryBuilderHelper.createSubcategory());
        discount = discountRepository.save(DiscountBuilderHelper.createDiscount(subcategory));
    }

    @AfterEach
    void deleteTestData() {
        discountRepository.deleteAll();
        subcategoryRepository.deleteAll();
    }

    @Test
    void shouldGetDiscountByCode_forEverybody_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<DiscountUserResponse> response = restTemplate.exchange(
                localhostUri + DISCOUNT_URI + "/test01",
                HttpMethod.GET,
                httpEntity,
               DiscountUserResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(discount.getDiscountRate().setScale(2), response.getBody().discountRate().setScale(2));
        assertTrue(response.getBody().subcategoryIds().containsAll(discount.getSubcategories().stream().map(Subcategory::getId).toList()));
    }

    @Test
    void shouldSaveDiscount_forAuthenticatedAdmin_thenStatusCreated() {
        DiscountRequest discountRequest = DiscountBuilderHelper.createDiscountRequestWithoutCode();
        HttpEntity<DiscountRequest> httpEntity = new HttpEntity<>(discountRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<DiscountAdminResponse> response = restTemplate.exchange(
                localhostAdminUri + DISCOUNT_URI,
                HttpMethod.POST,
                httpEntity,
                DiscountAdminResponse.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().code());
        assertEquals(discountRequest.quantity(), response.getBody().quantity());
        assertEquals(discountRequest.discountRate(), response.getBody().discountRate());
        assertEquals(discountRequest.endDate(), response.getBody().endDate());
        assertTrue(discountRequest.subcategoryNames().containsAll(response.getBody().subcategoryNames()));
    }

    @Test
    void shouldDeleteUsedOrExpiredDiscount_forAuthenticatedAdmin_thenStatusNoContent() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<Void> response = restTemplate.exchange(
                localhostAdminUri + DISCOUNT_URI,
                HttpMethod.DELETE,
                httpEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
