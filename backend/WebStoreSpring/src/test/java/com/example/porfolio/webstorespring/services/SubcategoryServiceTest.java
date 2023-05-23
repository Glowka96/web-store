package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.products.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.products.SubcategoryRepository;
import com.example.porfolio.webstorespring.services.products.SubcategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(subCategoryMapper, "productMapper", productMapper);
        ReflectionTestUtils.setField(subCategoryMapper, "categoryMapper", categoryMapper);

        category = new Category("Category");
        category.setId(1L);

        subCategory = new Subcategory("SubCategory");
        subCategory.setId(1L);
        subCategory.setCategory(category);

        subCategoryRequest = new SubcategoryRequest();
        subCategoryRequest.setId(1L);
        subCategoryRequest.setName("Test");
    }

    @Test
    void shouldGetSubCategoryById() {
        // given
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubcategoryRequest subCategoryRequest = underTest.getSubcategoryDtoById(1L);

        // then
        assertThat(subCategoryRequest).isNotNull();
        assertThat(subCategoryRequest.getName()).isEqualTo(subCategory.getName());
        verify(subCategoryRepository, times(1)).findById(subCategory.getId());
    }

    @Test
    void shouldSaveSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        SubcategoryRequest savedSubcategoryRequest = underTest.save(category.getId(), subCategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryRequest mappedSubCategory = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(savedSubcategoryRequest).isNotNull();
        assertThat(savedSubcategoryRequest.getCategoryResponse()).isNotNull();
        assertThat(savedSubcategoryRequest).isEqualTo(mappedSubCategory);
    }

    @Test
    void willThrowWhenSubCategoryNameIsNotFound() {
        // given
        Long notFoundId = 2L;
        given(subCategoryRepository.findById(notFoundId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSubcategoryDtoById(notFoundId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SubCategory with id 2 not found");
    }

    @Test
    void shouldUpdateSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubcategoryRequest updatedSubcategoryRequest = underTest.update(category.getId(), subCategory.getId(), subCategoryRequest);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryRequest mappedSubcategoryRequest = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(updatedSubcategoryRequest.getName()).isEqualTo(subCategoryRequest.getName());
        assertThat(updatedSubcategoryRequest.getCategoryResponse()).isNotNull();
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