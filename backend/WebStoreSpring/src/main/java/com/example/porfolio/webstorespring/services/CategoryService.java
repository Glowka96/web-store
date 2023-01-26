package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ErrorMSG;
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
    private final CategoryMapper mapper;

    public List<CategoryDto> getAllCategory() {
        return mapper.categoriesToCategoriesDto(
                categoryRepository.findAll());
    }

    public CategoryDto getCategoryById(Long id) {
        Category foundCategory = findCategoryById(id);
        return mapper.categoryToCategoryDto(foundCategory);
    }

    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryRepository.save(
                mapper.categoryDtoToCategory(categoryDto));
        category.getSubCategories().forEach(
                subCategory -> subCategory.setCategory(category));
        return mapper.categoryToCategoryDto(category);
    }

    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category findCategoryById = findCategoryById(id);

        Category category = mapper.categoryDtoToCategory(categoryDto);
        category.setId(findCategoryById.getId());

        Category saveCategory = categoryRepository.save(category);
        return mapper.categoryToCategoryDto(saveCategory);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMSG.CATEGORY_WITH_ID_NOT_FOUND.getMessage(), id)));
    }
}
