package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.models.dtos.products.requests.SubcategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.SubcategoryResponse;
import com.example.portfolio.webstorespring.models.entities.products.Category;
import com.example.portfolio.webstorespring.models.entities.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class SubcategoryServiceTest {
    @Mock
    private SubcategoryRepository subcategoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private SubcategoryService underTest;

    @Test
    void shouldGetAllSubcategoryResponse() {
        underTest.getAll();

        verify(subcategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(subcategoryRepository);
    }

    @Test
    void shouldFindSubcategoriesByNames() {
        given(subcategoryRepository.findAllByNames(anySet())).willReturn(Set.of(SubcategoryBuilderHelper.createSubcategory()));

        Set<Subcategory> foundSubcategories = underTest.findAllByNames(Set.of("Test"));

        assertEquals(1, foundSubcategories.size());
    }

    @Test
    void shouldSaveSubcategory() {
        Category category = createCategory();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest();
        given(categoryService.findById(anyLong())).willReturn(category);
        given(subcategoryRepository.save(any(Subcategory.class))).willReturn(createSubcategory());

        SubcategoryResponse savedSubcategoryResponse = underTest.save(category.getId(), subcategoryRequest);

        ArgumentCaptor<Subcategory> subcategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subcategoryRepository).save(subcategoryArgumentCaptor.capture());

        SubcategoryResponse mappedSubcategory =
                SubcategoryMapper.mapToResponse(subcategoryArgumentCaptor.getValue());

        assertEquals(mappedSubcategory, savedSubcategoryResponse);
    }

    @Test
    void shouldUpdateSubcategory() {
        Category category = createCategory();
        Subcategory subcategory = createSubcategory();
        String subcategoryNameBeforeUpdate = subcategory.getName();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest("Test2");

        given(categoryService.findById(category.getId())).willReturn(category);
        given(subcategoryRepository.findById(subcategory.getId())).willReturn(Optional.of(subcategory));

        SubcategoryResponse updatedSubcategoryRequest = underTest.update(category.getId(), subcategory.getId(), subcategoryRequest);

        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subcategoryRepository).save(subCategoryArgumentCaptor.capture());

        SubcategoryResponse mappedSubcategoryRequest =
                SubcategoryMapper.mapToResponse(subCategoryArgumentCaptor.getValue());

        assertEquals(mappedSubcategoryRequest, updatedSubcategoryRequest);
        assertNotEquals(subcategoryNameBeforeUpdate, updatedSubcategoryRequest.name());
    }

    @Test
    void shouldDeleteSubCategoryById() {
        underTest.deleteById(anyLong());

        verify(subcategoryRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(subcategoryRepository);
    }
}
