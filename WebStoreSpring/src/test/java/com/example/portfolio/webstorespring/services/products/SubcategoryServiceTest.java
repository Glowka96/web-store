package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class SubcategoryServiceTest {
    @Spy
    private SubcategoryMapper subCategoryMapper = Mappers.getMapper(SubcategoryMapper.class);
    @Mock
    private SubcategoryRepository subcategoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private SubcategoryService underTest;

    @Test
    void shouldGetSubcategoryById() {
        // given
        Subcategory subcategory = createSubcategory();
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));

        // when
        SubcategoryResponse foundSubcategoryResponse = underTest.getSubcategoryDtoById(1L);

        // then
        assertThat(foundSubcategoryResponse).isNotNull();
        assertThat(foundSubcategoryResponse.getName()).isEqualTo(subcategory.getName());
        verify(subcategoryRepository, times(1)).findById(subcategory.getId());
    }

    @Test
    void shouldGetAllSubcategoryResponse() {
        // given
        // when
        underTest.getAllSubcategory();

        // then
        verify(subcategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(subcategoryRepository);
    }

    @Test
    void shouldSaveSubcategory() {
        // given
        Category category = createCategory();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest();
        given(categoryService.findCategoryById(anyLong())).willReturn(category);

        // when
        SubcategoryResponse savedSubcategoryResponse = underTest.saveSubcategory(category.getId(), subcategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subcategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subcategoryRepository).save(subcategoryArgumentCaptor.capture());

        SubcategoryResponse mappedSubCategory =
                subCategoryMapper.mapToDto(subcategoryArgumentCaptor.getValue());

        assertThat(savedSubcategoryResponse).isEqualTo(mappedSubCategory);
    }

    @Test
    void willThrowWhenSubcategoryIdIsNotFound() {
        // given
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSubcategoryDtoById(anyLong()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Subcategory with id 0 not found");
    }

    @Test
    void shouldUpdateSubcategory() {
        // given
        Category category = createCategory();
        Subcategory subcategory = createSubcategory();
        String subcategoryName = subcategory.getName();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest("Test2");

        given(categoryService.findCategoryById(category.getId())).willReturn(category);
        given(subcategoryRepository.findById(subcategory.getId())).willReturn(Optional.of(subcategory));

        // when
        SubcategoryResponse updatedSubcategoryRequest = underTest.updateSubcategory(category.getId(), subcategory.getId(), subcategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subcategoryRepository).save(subCategoryArgumentCaptor.capture());

        SubcategoryResponse mappedSubcategoryRequest =
                subCategoryMapper.mapToDto(subCategoryArgumentCaptor.getValue());

        assertThat(updatedSubcategoryRequest).isEqualTo(mappedSubcategoryRequest);
        assertThat(updatedSubcategoryRequest.getName()).isNotEqualTo(subcategoryName);
    }

    @Test
    void shouldDeleteSubCategoryById() {
        // given
        Subcategory subcategory = createSubcategory();
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));

        // when
        underTest.deleteSubcategoryById(anyLong());

        // then
        verify(subcategoryRepository, times(1)).deleteById(subcategory.getId());
    }
}
