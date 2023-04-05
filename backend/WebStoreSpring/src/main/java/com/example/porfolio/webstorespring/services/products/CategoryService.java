package com.example.porfolio.webstorespring.services.products;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategoryDto() {
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
        Category foundCategory = findCategoryById(id);

        Category category = categoryMapper.mapToEntity(categoryDto);
        setupCategory(foundCategory, category);

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

    private void setupCategory(Category foundCategory, Category updatedCategory){
        updatedCategory.setId(foundCategory.getId());

        if(updatedCategory.getName() == null) {
            updatedCategory.setName(foundCategory.getName());
        }
        if(updatedCategory.getSubcategories() == null){
            updatedCategory.setSubcategories(foundCategory.getSubcategories());
        }
    }
}
