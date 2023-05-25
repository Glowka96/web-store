package com.example.porfolio.webstorespring.services.products;


import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.CategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.CategoryResponse;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.repositories.products.CategoryRepository;
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

    private CategoryRequest categoryRequest;
    private Category category;

    @BeforeEach
    void initialization() {
        category = new Category();
        category.setId(1L);
        category.setName("CategoryTest");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test");
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
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryResponse foundCategory = underTest.getCategoryDtoById(category.getId());

        // then
        assertThat(foundCategory).isNotNull();
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void willThrowWhenCategoryIdIsNotFound() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

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
        CategoryResponse savedCategoryResponse = underTest.save(categoryRequest);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryResponse mappedCategoryResponse = categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryResponse).isEqualTo(savedCategoryResponse);
    }

    @Test
    void shouldUpdateCategory() {
        // given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryResponse updatedCategoryResponse = underTest.update(category.getId(), categoryRequest);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryResponse mappedCategoryResponse = categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryResponse).isEqualTo(updatedCategoryResponse);
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