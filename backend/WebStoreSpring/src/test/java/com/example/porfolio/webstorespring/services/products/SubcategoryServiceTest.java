package com.example.porfolio.webstorespring.services.products;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryResponse;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.products.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private SubcategoryRepository subCategoryRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private SubcategoryService underTest;

    private Category category;
    private Subcategory subCategory;
    private SubcategoryRequest subCategoryRequest;

    @BeforeEach
    public void initialization() {
        category = new Category();
        category.setName("Test");
        category.setId(1L);

        subCategory = new Subcategory("SubCategory");
        subCategory.setId(1L);
        subCategory.setCategory(category);

        subCategoryRequest = new SubcategoryRequest();
        subCategoryRequest.setName("Test");
    }

    @Test
    void shouldGetSubCategoryById() {
        // given
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));

        // when
        SubcategoryResponse foundSubcategoryResponse = underTest.getSubcategoryDtoById(1L);

        // then
        assertThat(foundSubcategoryResponse).isNotNull();
        assertThat(foundSubcategoryResponse.getName()).isEqualTo(subCategory.getName());
        verify(subCategoryRepository, times(1)).findById(subCategory.getId());
    }

    @Test
    void shouldGetAllSubcategoryResponse() {
        // given
        // when
        underTest.getAllSubcategoryResponse();

        // then
        verify(subCategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(subCategoryRepository);
    }

    @Test
    void shouldSaveSubCategory() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        SubcategoryResponse savedSubcategoryResponse = underTest.save(category.getId(), subCategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryResponse mappedSubCategory = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(savedSubcategoryResponse).isNotNull();
        assertThat(savedSubcategoryResponse).isEqualTo(mappedSubCategory);
    }

    @Test
    void willThrowWhenSubCategoryNameIsNotFound() {
        // given
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSubcategoryDtoById(anyLong()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SubCategory with id 0 not found");
    }

    @Test
    void shouldUpdateSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubcategoryResponse updatedSubcategoryRequest = underTest.update(category.getId(), subCategory.getId(), subCategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryResponse mappedSubcategoryRequest = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(updatedSubcategoryRequest.getName()).isEqualTo(subCategoryRequest.getName());
        assertThat(updatedSubcategoryRequest).isEqualTo(mappedSubcategoryRequest);
    }

    @Test
    void shouldDeleteSubCategoryById() {
        // given
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        underTest.deleteSubcategoryById(subCategory.getId());

        // then
        verify(subCategoryRepository, times(1)).deleteById(subCategory.getId());
    }
}