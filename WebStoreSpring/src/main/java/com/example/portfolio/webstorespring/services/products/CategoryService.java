package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategoryDto() {
        return categoryMapper.mapToDto(
                categoryRepository.findAll());
    }

    public CategoryResponse getCategoryDtoById(Long id) {
        Category foundCategory = findCategoryById(id);
        return categoryMapper.mapToDto(foundCategory);
    }

    public CategoryResponse save(CategoryRequest categoryRequest) {
        Category category = categoryMapper.mapToEntity(categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Category foundCategory = findCategoryById(id);

        Category category = categoryMapper.mapToEntity(categoryRequest);
        setupUpdateCategory(foundCategory, category);

        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    public void deleteById(Long id) {
        Category category = findCategoryById(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private void setupUpdateCategory(Category foundCategory, Category updatedCategory){
        updatedCategory.setId(foundCategory.getId());

        if(updatedCategory.getName() == null) {
            updatedCategory.setName(foundCategory.getName());
        }
        if(updatedCategory.getSubcategories() == null){
            updatedCategory.setSubcategories(foundCategory.getSubcategories());
        }
    }
}
