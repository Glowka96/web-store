package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.IT.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductsPageControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private InitProductTestData initProductTestData;

    @BeforeEach
    void initTestData() {
        initProductTestData.initTestData();
    }

    @AfterEach
    void deleteTestData() {
        initProductTestData.deleteTestData();
    }

    @Test
    void shouldGetPageProductsBySubcategoryId_forEverybody_thenStatusOk() {
        HttpEntity<PageProductsOptions> httpEntity = new HttpEntity<>(new PageProductsOptions(0, 12, "type - desc"));

        ResponseEntity<PageProductsWithPromotionDTO> response =
                sendRequest("/subcategories/" + initProductTestData.getSubId() + "/products", httpEntity);

        assertPageResponse(response);
    }

    @Test
    void shouldGetPagePromotionProduct_forEverybody_thenStatusOk() {
        HttpEntity<PageProductsOptions> httpEntity = new HttpEntity<>(new PageProductsOptions(0, 12, "name - asc"));

        ResponseEntity<PageProductsWithPromotionDTO> response =
                sendRequest("/products/promotions", httpEntity);

        assertPageResponse(response);
        assertThat(Objects.requireNonNull(response.getBody()).products().get(0).name().charAt(0))
                .isLessThan(response.getBody().products().get(
                                getLastProductIndex(response)).name().charAt(0)
                );
    }

    @Test
    void shouldGetPageNewProducts_forEverybody_thenStatusOk() {
        HttpEntity<PageProductsOptions> httpEntity = new HttpEntity<>(new PageProductsOptions(0, 12, "price - desc"));

        ResponseEntity<PageProductsWithPromotionDTO> response =
                sendRequest("/products/news", httpEntity);

        assertPageResponse(response);
        assertThat(Objects.requireNonNull(response.getBody()).products().get(0).price())
                .isGreaterThan(response.getBody().products().get(
                       getLastProductIndex(response)).price()
                );
    }

    private ResponseEntity<PageProductsWithPromotionDTO> sendRequest(String uri, HttpEntity<PageProductsOptions> httpEntity) {
        return restTemplate.exchange(
                localhostUri + uri,
                HttpMethod.GET,
                httpEntity,
                PageProductsWithPromotionDTO.class
        );
    }
    private static void assertPageResponse(ResponseEntity<PageProductsWithPromotionDTO> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sortOptions()).hasSize(8);
    }

    private int getLastProductIndex(ResponseEntity<PageProductsWithPromotionDTO> response) {
        return Objects.requireNonNull(response.getBody()).products().size() - 1;
    }
}