package com.example.porfolio.webstorespring.services;


import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class CategoryServiceTest {
    @Spy
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService underTest;

    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    void initialization() {
        category = new Category();
        category.setId(1L);
        category.setName("CategoryTest");

        categoryDto = new CategoryDto();
        categoryDto.setName("Test");
    }

    @Test
    void shouldGetAllCategoryDto() {
        // when
        underTest.getAllCategoryDto();
        // then
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);

    }

    @Test
    void shouldGetCategoryById() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        CategoryDto foundCategory = underTest.getCategoryDtoById(category.getId());

        // then
        assertThat(foundCategory).isNotNull();
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void willThrowWhenCategoryIdIsNotFound() {
        // given
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getCategoryDtoById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category with id 2 not found");
    }

    @Test
    void willThrowWhenCategoryNameIsNotFound() {
        // given
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getCategoryDtoById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category with id 2 not found");
    }

    @Test
    void shouldSaveCategory() {
        // given
        // when
        underTest.save(categoryDto);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryDto mappedCategoryDto = categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryDto).isEqualTo(categoryDto);
    }

    @Test
    void shouldUpdateCategory() {
        // given
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        // when
        underTest.update(category.getId(), categoryDto);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryDto mappedCategoryDto = categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryDto.getName()).isEqualTo(categoryDto.getName());
        assertThat(mappedCategoryDto.getId()).isEqualTo(category.getId());
    }

    @Test
    void shouldDeleteCategoryById() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        underTest.deleteById(1L);

        // then
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(category);
    }
}