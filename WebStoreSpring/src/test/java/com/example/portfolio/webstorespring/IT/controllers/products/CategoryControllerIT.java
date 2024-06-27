package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    private static final String ADMIN_URI = "/api/v1/admin/categories";
    private Long categoryId;
    @BeforeEach
    @Override
    public void init() {
        categoryRepository.deleteAll();
        Category savedCategory = categoryRepository.save(CategoryBuilderHelper.createCategory());
        categoryId = savedCategory.getId();
        super.init();
    }

    @Test
    void shouldGetAllCategory() {
        ResponseEntity<List<CategoryResponse>> response = restTemplate.exchange(LOCALHOST_URI + "/api/v1/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void shouldSaveCategory_forAuthenticatedAdmin_thenStatusCreated() {
        Category category = CategoryBuilderHelper.createCategory();

        HttpEntity<Category> entity = new HttpEntity<>(category, getHttpHedearsWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI,
                HttpMethod.POST,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }

    @Test
     void shouldNotSaveCategory_forAuthenticatedUser_thenStatusForbidden() {
        Category category = CategoryBuilderHelper.createCategory();

        HttpEntity<Category> entity = new HttpEntity<>(category, getHttpHeaderWithUserToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI,
                HttpMethod.POST,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(1);
    }

    @Test
    void shouldUpdateCategory_forAuthenticatedAdmin_thenStatusAccepted() {
        Category category = CategoryBuilderHelper.createCategory("Update name");

        HttpEntity<Category> entity = new HttpEntity<>(category, getHttpHedearsWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI + "/" + categoryId,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory.get().getName()).isEqualTo(category.getName());
    }

    @Test
    void shouldNotUpdateCategory_forAuthenticatedUser_thenStatusForbidden() {
        Category category = CategoryBuilderHelper.createCategory("Update name");

        HttpEntity<Category> entity = new HttpEntity<>(category, getHttpHeaderWithUserToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI + "/" + categoryId,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory.get().getName()).isNotEqualTo(category.getName());
    }

    @Test
    void shouldDeleteCategory_forAuthenticatedAdmin_thanStatusNotContent() {
        HttpEntity<Category> entity = new HttpEntity<>(getHttpHedearsWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI + "/" + categoryId,
                HttpMethod.DELETE,
                entity,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory).isNotPresent();
    }

    @Test
    void shouldNotDeleteCategory_forAuthenticatedUser_thanStatusForbidden() {
        HttpEntity<Category> entity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_URI + ADMIN_URI + "/" + categoryId,
                HttpMethod.DELETE,
                entity,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory).isPresent();
    }
}
