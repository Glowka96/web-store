package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerIT extends AbstractBaseControllerIT<CategoryRequest, CategoryResponse, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @BeforeEach
    public void initTestData() {
        Subcategory subcategory1 = createSubcategory("One");
        Subcategory subcategory2 = createSubcategory("Two");
        Subcategory subcategory3 = createSubcategory("Three");

        Category category = createCategory();
        category.setSubcategories(Arrays.asList(subcategory1, subcategory2, subcategory3));
        category.getSubcategories().forEach(s -> s.setCategory(category));
        Category savedCategory = categoryRepository.save(category);
        savedEntityId = savedCategory.getId();
    }

    @Override
    @AfterEach
    public void deleteTestData() {
        categoryRepository.deleteAll();
    }

    public String getUri() {
        return "/categories";
    }

    @Override
    public CategoryRequest createRequest() {
        return CategoryBuilderHelper.createCategoryRequest("Test category");
    }

    @Override
    public Class<CategoryResponse> getResponseTypeClass() {
        return CategoryResponse.class;
    }

    @Override
    public List<Category> getAllEntities() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getOptionalEntityBySavedId() {
        return categoryRepository.findById(savedEntityId);
    }

    @Override
    public ParameterizedTypeReference<List<CategoryResponse>> getListResponseTypeClass() {
        return new ParameterizedTypeReference<>() {
        };
    }

    @Override
    public void assertsFieldsWhenGetAll(List<CategoryResponse> responses) {
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getSubcategoryResponses()).hasSize(3);
    }

    @Override
    public void assertsFieldsWhenSave(CategoryRequest request,
                                      CategoryResponse response) {
        Optional<Category> optionalCategory = categoryRepository.findById(response.getId());
        assertThat(optionalCategory).isPresent();

        assertThat(response.getId()).isNotNull().isEqualTo(optionalCategory.get().getId());
        assertThat(response.getName()).isEqualTo(request.getName())
                .isEqualTo(optionalCategory.get().getName());
    }

    @Override
    public void assertsFieldsWhenUpdate(CategoryRequest request,
                                        CategoryResponse response,
                                        Category entityBeforeUpdate,
                                        Category entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.getId())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.getName())
                .isEqualTo(response.getName())
                .isNotEqualTo(entityBeforeUpdate.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(CategoryRequest request, Category entity) {
        assertThat(entity.getName()).isNotEqualTo(request.getName());
    }

    @Test
    void shouldGetAllCategory_forEverybody_thenStatusOk() {
        shouldGetAllEntities_forEverybody_thenStatusOk();
    }

    @Test
    void shouldSaveCategory_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveCategory_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateCategory_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateCategory_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteCategory_forAuthenticatedAdmin_thanStatusNotContent() {
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteCategory_forAuthenticatedUser_thanStatusForbidden() {
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
