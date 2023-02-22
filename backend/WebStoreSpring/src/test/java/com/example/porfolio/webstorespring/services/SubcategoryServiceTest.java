package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.SubcategoryRepository;
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
    private SubcategoryDto subCategoryDto;

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

        subCategoryDto = new SubcategoryDto();
        subCategoryDto.setId(1L);
        subCategoryDto.setName("Test");
    }

    @Test
    void shouldGetSubCategoryById() {
        // given
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubcategoryDto subCategoryDto = underTest.getSubcategoryDtoById(1L);

        // then
        assertThat(subCategoryDto).isNotNull();
        assertThat(subCategoryDto.getName()).isEqualTo(subCategory.getName());
        verify(subCategoryRepository, times(1)).findById(subCategory.getId());
    }

    @Test
    void shouldSaveSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        SubcategoryDto savedSubcategoryDto = underTest.save(category.getId(), subCategoryDto);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryDto mappedSubCategory = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(savedSubcategoryDto).isNotNull();
        assertThat(savedSubcategoryDto.getCategoryDto()).isNotNull();
        assertThat(savedSubcategoryDto).isEqualTo(mappedSubCategory);
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
        SubcategoryDto updatedSubcategoryDto = underTest.update(category.getId(), subCategory.getId(), subCategoryDto);

        // then
        ArgumentCaptor<Subcategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(Subcategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        Subcategory capturedSubcategory = subCategoryArgumentCaptor.getValue();
        SubcategoryDto mappedSubcategoryDto = subCategoryMapper.mapToDto(capturedSubcategory);

        assertThat(updatedSubcategoryDto.getName()).isEqualTo(subCategoryDto.getName());
        assertThat(updatedSubcategoryDto.getCategoryDto()).isNotNull();
        assertThat(updatedSubcategoryDto).isEqualTo(mappedSubcategoryDto);
    }

    @Test
    void shouldDeleteSubCategoryById() {
        // given
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        underTest.deleteSubcategory(subCategory.getId());

        // then
        verify(subCategoryRepository, times(1)).deleteById(subCategory.getId());
    }
}