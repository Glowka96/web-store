package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.models.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.models.entity.products.Category;
import com.example.portfolio.webstorespring.models.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerIT extends AbstractBaseControllerIT<CategoryRequest, CategoryResponse, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @BeforeEach
    public void initTestData() {
        Subcategory subcategory1 = createSubcategoryWithoutId("One");
        Subcategory subcategory2 = createSubcategoryWithoutId("Two");
        Subcategory subcategory3 = createSubcategoryWithoutId("Three");

        Category category = CategoryBuilderHelper.createCategory();
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
        assertEquals(1, responses.size());
        assertEquals(3, responses.get(0).subcategoryResponses().size());
    }

    @Override
    public void assertsFieldsWhenSave(CategoryRequest request,
                                      CategoryResponse response) {
        Optional<Category> optionalCategory = categoryRepository.findById(response.id());
        assertTrue(optionalCategory.isPresent());

        assertThat(response.id()).isNotNull().isEqualTo(optionalCategory.get().getId());
        assertEquals(request.name(), optionalCategory.get().getName(), response.name());
    }

    @Override
    public void assertsFieldsWhenUpdate(CategoryRequest request,
                                        CategoryResponse response,
                                        Category entityBeforeUpdate,
                                        Category entityAfterUpdate) {
        assertThat(entityAfterUpdate.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.id())
                .isEqualTo(entityBeforeUpdate.getId());
        assertThat(entityAfterUpdate.getName()).isEqualTo(request.name())
                .isEqualTo(response.name())
                .isNotEqualTo(entityBeforeUpdate.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(CategoryRequest request, Category entity) {
        assertNotEquals(request.name(), entity.getName());
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
    void shouldUpdateCategory_forAuthenticatedAdmin_thenStatusOK() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusOK();
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
