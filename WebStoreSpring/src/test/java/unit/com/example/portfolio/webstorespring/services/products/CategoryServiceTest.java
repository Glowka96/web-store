package com.example.portfolio.webstorespring.services.products;


import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.models.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.models.entity.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService underTest;

    @Test
    void shouldGetAllCategory() {
        List<Category> categories = Collections.singletonList(createCategory());
        List<CategoryResponse> exceptedResponses = Collections.singletonList(createCategoryResponse());

        given(categoryRepository.findAll()).willReturn(categories);

        List<CategoryResponse> foundCategoryResponses = underTest.getAll();

        assertEquals(exceptedResponses, foundCategoryResponses);
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldSaveCategory() {
        CategoryRequest categoryRequest = createCategoryRequest();

        CategoryResponse savedCategoryResponse = underTest.save(categoryRequest);

        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        CategoryResponse mappedCategoryResponse =
                CategoryMapper.mapToDto(categoryArgumentCaptor.getValue());

        assertEquals(mappedCategoryResponse, savedCategoryResponse);
    }

    @Test
    void shouldUpdateCategory() {
        Category category = createCategory();
        String categoryNameBeforeUpdate = category.getName();
        CategoryRequest categoryRequest = createCategoryRequest("Test2");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        CategoryResponse updatedCategoryResponse = underTest.update(category.getId(), categoryRequest);

        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        CategoryResponse mappedCategoryResponse =
                CategoryMapper.mapToDto(categoryArgumentCaptor.getValue());

        assertEquals(mappedCategoryResponse, updatedCategoryResponse);
        assertNotEquals(categoryNameBeforeUpdate, updatedCategoryResponse.name());
    }

    @Test
    void shouldDeleteCategoryById() {
        underTest.deleteById(anyLong());

        verify(categoryRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }
}
