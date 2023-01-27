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
        Category foundCategory = findCategoryById(id);
        return categoryMapper.mapToDto(foundCategory);
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

        Category saveCategory = categoryRepository.save(category);
        return categoryMapper.mapToDto(saveCategory);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", id));
    }
}
