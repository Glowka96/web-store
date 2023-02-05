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
        // given
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubCategoryDto savedSubCategoryDto = underTest.getSubCategoryDtoById(subCategory.getId());

        // then
        assertThat(savedSubCategoryDto).isNotNull();
        assertThat(savedSubCategoryDto.getName()).isEqualTo(subCategory.getName());
        verify(subCategoryRepository, times(1)).findById(subCategory.getId());
    }

    @Test
    void shouldSaveSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        SubCategoryDto savedSubCategoryDto = underTest.save(category.getId(), subCategoryDto);

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
        Long notFoundId = 2L;
        given(subCategoryRepository.findById(notFoundId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSubCategoryDtoById(notFoundId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SubCategory with id 2 not found");
    }

    @Test
    void shouldUpdateSubCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        SubCategoryDto updatedSubCategoryDto = underTest.update(category.getId(), subCategory.getId(), subCategoryDto);

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
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.of(subCategory));

        // when
        underTest.deleteSubCategory(subCategory.getId());

        // then
        verify(subCategoryRepository, times(1)).deleteById(subCategory.getId());
    }
}