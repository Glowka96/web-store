package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.mappers.SubCategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubCategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.SubCategoryRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class SubCategoryServiceTest {
    @Spy
    private SubCategoryMapper subCategoryMapper = Mappers.getMapper(SubCategoryMapper.class);
    @Mock
    private SubCategoryRepository subCategoryRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private SubCategoryService underTest;

    private Category category;
    private SubCategory subCategory;
    private SubCategoryDto subCategoryDto;

    @BeforeEach
    public void initialization() {
        CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
        ReflectionTestUtils.setField(subCategoryMapper, "categoryMapper", categoryMapper);

        category = new Category("Category");
        category.setId(1L);

        subCategory = new SubCategory("SubCategory");
        subCategory.setId(1L);
        subCategory.setCategory(category);

        subCategoryDto = new SubCategoryDto();
        subCategoryDto.setId(1L);
        subCategoryDto.setName("Test");
    }

    @Test
    void shouldGetSubCategoryById() {
    }

    @Test
    void shouldGetSubCategoryByName() {
        // given
        given(subCategoryRepository.findByName(subCategory.getName())).willReturn(Optional.of(subCategory));

        // when
        SubCategoryDto savedSubCategoryDto = underTest.getSubCategoryDtoByName(subCategory.getName());

        // then
        assertThat(savedSubCategoryDto).isNotNull();
        assertThat(savedSubCategoryDto.getName()).isEqualTo(subCategory.getName());
        verify(subCategoryRepository, times(1)).findByName(subCategory.getName());
    }

    @Test
    void shouldSaveSubCategory() {
        // given
        given(categoryRepository.findByName(category.getName())).willReturn(Optional.of(category));
        // when
        SubCategoryDto savedSubCategoryDto = underTest.save(category.getName(), subCategoryDto);

        // then
        ArgumentCaptor<SubCategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(SubCategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        SubCategory capturedSubCategory = subCategoryArgumentCaptor.getValue();
        SubCategoryDto mappedSubCategory = subCategoryMapper.mapToDto(capturedSubCategory);

        assertThat(savedSubCategoryDto).isNotNull();
        assertThat(savedSubCategoryDto.getCategoryDto()).isNotNull();
        assertThat(savedSubCategoryDto).isEqualTo(mappedSubCategory);
    }

    @Test
    void willThrowWhenSubCategoryNameIsNotFound() {
        // given
        String notFoundName = "error";
        given(subCategoryRepository.findByName(notFoundName)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSubCategoryDtoByName(notFoundName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SubCategory with name error not found");
    }

    @Test
    void shouldUpdateSubCategory() {
        // given
        given(categoryRepository.findByName(category.getName())).willReturn(Optional.of(category));
        given(subCategoryRepository.findByName(subCategory.getName())).willReturn(Optional.of(subCategory));

        // when
        SubCategoryDto updatedSubCategoryDto = underTest.update(category.getName(), subCategory.getName(), subCategoryDto);

        // then
        ArgumentCaptor<SubCategory> subCategoryArgumentCaptor =
                ArgumentCaptor.forClass(SubCategory.class);
        verify(subCategoryRepository).save(subCategoryArgumentCaptor.capture());

        SubCategory capturedSubCategory = subCategoryArgumentCaptor.getValue();
        SubCategoryDto mappedSubCategoryDto = subCategoryMapper.mapToDto(capturedSubCategory);

        assertThat(updatedSubCategoryDto.getName()).isEqualTo(subCategoryDto.getName());
        assertThat(updatedSubCategoryDto.getCategoryDto()).isNotNull();
        assertThat(updatedSubCategoryDto).isEqualTo(mappedSubCategoryDto);
    }

    @Test
    void shouldDeleteSubCategory() {
        // given
        given(subCategoryRepository.findByName(subCategory.getName())).willReturn(Optional.of(subCategory));

        // when
        underTest.deleteSubCategory(subCategory.getName());

        // then
        verify(subCategoryRepository, times(1)).deleteByName(subCategory.getName());
    }
}