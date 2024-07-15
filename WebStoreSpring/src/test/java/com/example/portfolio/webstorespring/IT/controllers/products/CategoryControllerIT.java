package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategory;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CategoryControllerIT extends AbstractBaseControllerIT<CategoryRequest, CategoryResponse, Category> {

    @Autowired
    private CategoryRepository categoryRepository;
    private static final String CATEGORY_URI = "/categories";

    @Override
    protected String getUri() {
        return "/categories";
    }

    @Override
    protected CategoryRequest createRequest() {
        return CategoryBuilderHelper.createCategoryRequest("Test category");
    }

    @Override
    protected Class<CategoryResponse> getResponseTypeClass() {
        return CategoryResponse.class;
    }

    @Override
    protected List<Category> getAllEntities() {
        return categoryRepository.findAll();
    }

    @Override
    protected Optional<Category> getOptionalEntityById() {
        return categoryRepository.findById(id);
    }

    @Override
    protected void setup() {
        categoryRepository.deleteAll();

        Subcategory subcategory1 = SubcategoryBuilderHelper.createSubcategory("One");
        Subcategory subcategory2 = SubcategoryBuilderHelper.createSubcategory("Two");
        Subcategory subcategory3 = SubcategoryBuilderHelper.createSubcategory("Three");

        Category category = createCategory();
        category.setSubcategories(Arrays.asList(subcategory1, subcategory2, subcategory3));
        category.getSubcategories().forEach(s -> s.setCategory(category));
        Category savedCategory = categoryRepository.save(category);
        id = savedCategory.getId();
    }

    @Test
    void shouldGetAllCategory() {
        ResponseEntity<List<CategoryResponse>> response = restTemplate.exchange(
                LOCALHOST_URI + CATEGORY_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getSubcategoryResponses()).hasSize(3);
    }

    @Test
    void shouldSaveCategory_forAuthenticatedAdmin_thenStatusCreated() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
       shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveCategory_forAuthenticatedUser_thenStatusForbidden() {
       shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateCategory_forAuthenticatedAdmin_thenStatusAccepted() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateCategory_forAuthenticatedUser_thenStatusForbidden() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        shouldNotUpdateEntityForAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteCategory_forAuthenticatedAdmin_thanStatusNotContent() {
        shouldDeleteEntityForAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteCategory_forAuthenticatedUser_thanStatusForbidden() {
        shouldNotDeleteEntityForAuthenticatedUser_thenStatusForbidden();
    }
}
