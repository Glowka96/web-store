package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category foundCategory = findCategoryById(id);

        Category category = categoryMapper.mapToEntity(categoryRequest);

        foundCategory.setName(category.getName());

        categoryRepository.save(foundCategory);
        return categoryMapper.mapToDto(foundCategory);
    }

    public void deleteCategoryById(Long id) {
        Category category = findCategoryById(id);
        categoryRepository.delete(category);
    }

    protected Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
