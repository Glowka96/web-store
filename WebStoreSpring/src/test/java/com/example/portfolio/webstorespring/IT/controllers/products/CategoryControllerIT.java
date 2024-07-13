package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategoryRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class CategoryControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private CategoryRepository categoryRepository;
    private static final String CATEGORY_URI = "/categories";
    private Long categoryId;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();


    @BeforeEach
    @Override
    public void init() {
        categoryRepository.deleteAll();

        Subcategory subcategory1 = SubcategoryBuilderHelper.createSubcategory("One");
        Subcategory subcategory2 = SubcategoryBuilderHelper.createSubcategory("Two");
        Subcategory subcategory3 = SubcategoryBuilderHelper.createSubcategory("Three");

        Category category = createCategory();
        category.setSubcategories(Arrays.asList(subcategory1, subcategory2, subcategory3));
        category.getSubcategories().forEach(s -> s.setCategory(category));
        Category savedCategory = categoryRepository.save(category);
        categoryId = savedCategory.getId();

        super.init();
    }

    @Test
    void shouldGetAllCategory() {
        ResponseEntity<List<CategoryResponse>> response = restTemplate.exchange(LOCALHOST_URI + CATEGORY_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getSubcategoryResponses()).hasSize(3);
    }

    @Test
    void shouldSaveCategory_forAuthenticatedAdmin_thenStatusCreated() {
        CategoryRequest categoryRequest = createCategoryRequest("New category");

        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + CATEGORY_URI,
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
        CategoryRequest categoryRequest = createCategoryRequest("New category");

        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, getHttpHeaderWithUserToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + CATEGORY_URI,
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
        CategoryRequest categoryRequest = createCategoryRequest("Update name");

        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, getHttpHeadersWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + CATEGORY_URI + "/" + categoryId,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory.get().getName()).isEqualTo(categoryRequest.getName());
    }

    @Test
    void shouldNotUpdateCategory_forAuthenticatedUser_thenStatusForbidden() {
        CategoryRequest categoryRequest = createCategoryRequest("Update name");

        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, getHttpHeaderWithUserToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + CATEGORY_URI + "/" + categoryId,
                HttpMethod.PUT,
                entity,
                CategoryResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory.get().getName()).isNotEqualTo(categoryRequest.getName());
    }

    @Test
    void shouldDeleteCategory_forAuthenticatedAdmin_thanStatusNotContent() {
        HttpEntity<Category> entity = new HttpEntity<>(getHttpHeadersWithAdminToken());

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(
                LOCALHOST_ADMIN_URI + CATEGORY_URI + "/" + categoryId,
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
                LOCALHOST_ADMIN_URI + CATEGORY_URI + "/" + categoryId,
                HttpMethod.DELETE,
                entity,
                CategoryResponse.class
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        assertThat(foundCategory).isPresent();
    }
}
