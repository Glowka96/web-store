package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
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

    public List<CategoryResponse> getAllCategory() {
        return categoryMapper.mapToDto(
                categoryRepository.findAll());
    }

    public CategoryResponse getCategoryById(Long id) {
        Category foundCategory = findCategoryById(id);
        return categoryMapper.mapToDto(foundCategory);
    }

    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.mapToEntity(categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category foundCategory = findCategoryById(id);

        Category category = categoryMapper.mapToEntity(categoryRequest);
        setupUpdateCategory(foundCategory, category);

        categoryRepository.save(category);
        return categoryMapper.mapToDto(category);
    }

    public void deleteCategoryById(Long id) {
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
