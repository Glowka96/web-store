package com.example.porfolio.webstorespring.services;


import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
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

    @Test
    void shouldGetAllCategoryDto() {
        // when
        underTest.getAllCategory();

        // then
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldGetCategoryById() {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test");

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        // when
        CategoryDto savedCategory = underTest.getCategoryById(category.getId());

        // then
        assertThat(savedCategory).isNotNull();
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void willThrowWhenIdIsNotFound() {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test");

        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getCategoryById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category with id 2 not found");
    }

    @Test
    void shouldAddCategory() {
        // given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test");

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
        Category category = new Category("CategoryTest");
        category.setId(1L);

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test");

        Long wantedId = 1L;

        // when
        underTest.update(wantedId, categoryDto);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryDto mappedCategoryDto = categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryDto.getName()).isEqualTo(categoryDto.getName());



    }

  /*  @Test
    void shouldFindCategoryDtoById() {

    }

    @Test
    void shouldCategoryDto() {
    }*/
}