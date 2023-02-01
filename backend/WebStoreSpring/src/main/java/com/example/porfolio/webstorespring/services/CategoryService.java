package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategory() {
        return categoryMapper.mapToDto(
                categoryRepository.findAll());
    }

    public CategoryDto getCategoryDtoById(Long id) {
        return categoryMapper.mapToDto(findCategoryById(id));
    }

    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.mapToEntity(categoryDto);
        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category findCategoryById = findCategoryById(id);

        Category category = categoryMapper.mapToEntity(categoryDto);
        category.setId(findCategoryById.getId());

        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", id));
    }
}
