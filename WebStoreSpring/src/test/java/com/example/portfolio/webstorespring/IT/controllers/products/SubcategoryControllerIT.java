package com.example.portfolio.webstorespring.IT.controllers.products;

import com.example.portfolio.webstorespring.IT.controllers.AbstractBaseControllerIT;
import com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryRequest;
import static org.assertj.core.api.Assertions.assertThat;

class SubcategoryControllerIT extends AbstractBaseControllerIT<SubcategoryRequest, SubcategoryResponse, Subcategory> {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private String uri;
    private Long savedCategoryId;

    @Override
    @BeforeEach
    public void initTestData() {
        Subcategory savedSubcategory = subcategoryRepository.save(createSubcategory());
        savedEntityId = savedSubcategory.getId();
        Category savedCategory = categoryRepository.save(CategoryBuilderHelper.createCategory());
        savedCategoryId = savedCategory.getId();

        uri = "/categories/" + savedCategoryId + "/subcategories";
    }

    @Override
    @AfterEach
    public void deleteTestData() {
        subcategoryRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public SubcategoryRequest createRequest() {
        return createSubcategoryRequest("Test name");
    }

    @Override
    public Class<SubcategoryResponse> getResponseTypeClass() {
        return SubcategoryResponse.class;
    }

    @Override
    public List<Subcategory> getAllEntities() {
        return subcategoryRepository.findAll();
    }

    @Override
    public Optional<Subcategory> getOptionalEntityBySavedId() {
        return subcategoryRepository.findById(savedEntityId);
    }

    @Override
    public void assertsFieldsWhenSave(SubcategoryRequest request,
                                      SubcategoryResponse response) {
        Optional<Subcategory> optionalSubcategory =
                subcategoryRepository.findById(response.getId());
        assertThat(optionalSubcategory).isPresent();

        assertThat(response.getId()).isNotNull()
                .isEqualTo(optionalSubcategory.get().getId());
        assertThat(response.getName()).isEqualTo(request.getName())
                .isEqualTo(optionalSubcategory.get().getName());
    }

    @Override
    public void assertsFieldsWhenUpdate(SubcategoryRequest request,
                                        SubcategoryResponse response,
                                        Subcategory entity) {
        assertThat(entity.getId()).isEqualTo(savedEntityId)
                .isEqualTo(response.getId());
        assertThat(entity.getCategory().getId()).isEqualTo(savedCategoryId);
        assertThat(entity.getName()).isEqualTo(request.getName())
                .isEqualTo(response.getName());
    }

    @Override
    public void assertsFieldsWhenNotUpdate(SubcategoryRequest request,
                                           Subcategory entity) {
        assertThat(entity.getName()).isNotEqualTo(request.getName());
    }

    @Test
    void shouldGetAllSubcategory_forAuthenticatedAdmin_thenStatusOk() {
        uri = "/categories/subcategories";
        shouldGetAllEntities_forAdmin_thenStatusOK();
    }

    @Test
    void shouldSaveSubcategory_forAuthenticatedAdmin_thenStatusCreated() {
        shouldSaveEntity_forAuthenticatedAdmin_thenStatusCreated();
    }

    @Test
    void shouldNotSaveSubcategory_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotSaveEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldUpdateSubcategory_forAuthenticatedAdmin_thenStatusAccepted() {
        shouldUpdateEntity_forAuthenticatedAdmin_thenStatusAccepted();
    }

    @Test
    void shouldNotUpdateSubcategory_forAuthenticatedUser_thenStatusForbidden() {
        shouldNotUpdateEntity_forAuthenticatedUser_thenStatusForbidden();
    }

    @Test
    void shouldDeleteSubcategory_forAuthenticatedAdmin_thenStatusNoContent() {
        uri = "/categories/subcategories";
        shouldDeleteEntity_forAuthenticatedAdmin_thenStatusNoContent();
    }

    @Test
    void shouldNotDeleteSubcategory_forAuthenticatedUser_thenStatusForbidden() {
        uri = "/categories/subcategories";
        shouldNotDeleteEntity_forAuthenticatedUser_thenStatusForbidden();
    }
}
