package com.example.portfolio.webstorespring.services.products;


import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
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
    void shouldGetAllCategory() {
        // given
        List<Category> categories = Collections.singletonList(createCategory());
        List<CategoryResponse> exceptedResponses = Collections.singletonList(createCategoryResponse());

        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryResponse> foundCategoryResponses = underTest.getAllCategory();

        // then
        assertThat(foundCategoryResponses).isEqualTo(exceptedResponses);
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
        verify(categoryMapper, times(1)).mapToDto(categories);
    }

    @Test
    void shouldSaveCategory() {
        // given
        CategoryRequest categoryRequest = createCategoryRequest();

        // when
        CategoryResponse savedCategoryResponse = underTest.saveCategory(categoryRequest);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        CategoryResponse mappedCategoryResponse =
                categoryMapper.mapToDto(capturedCategory);

        assertThat(mappedCategoryResponse).isEqualTo(savedCategoryResponse);
    }

    @Test
    void shouldUpdateCategory() {
        // given
        Category category = createCategory();
        String categoryName = category.getName();
        CategoryRequest categoryRequest = createCategoryRequest("Test2");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        CategoryResponse updatedCategoryResponse = underTest.updateCategory(category.getId(), categoryRequest);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        CategoryResponse mappedCategoryResponse =
                categoryMapper.mapToDto(categoryArgumentCaptor.getValue());

        assertThat(mappedCategoryResponse).isEqualTo(updatedCategoryResponse);
        assertThat(updatedCategoryResponse.getName()).isNotEqualTo(categoryName);
    }

    @Test
    void shouldDeleteCategoryById() {
        // given
        Category category = createCategory();
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        underTest.deleteCategoryById(1L);

        // then
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(category);
    }
}
