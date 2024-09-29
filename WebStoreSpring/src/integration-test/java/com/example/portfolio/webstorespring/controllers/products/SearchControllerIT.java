package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchControllerIT extends AbstractTestRestTemplateIT {

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
    void shouldGetPageSearchProductsByTest_forEveryBody_thenStatusOK() {
        HttpEntity<PageProductsOptions> httpEntity = new HttpEntity<>(new PageProductsOptions(0, 12, "date - desc"));

        ResponseEntity<PageProductsWithPromotionDTO> response = restTemplate.exchange(
                localhostUri + "/products/search?query=Education",
                HttpMethod.GET,
                httpEntity,
                PageProductsWithPromotionDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sortOptions()).hasSize(8);
        assertThat(response.getBody().products()).hasSize(1);
    }
}
