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

    public List<CategoryResponse> getAll() {
        return CategoryMapper.mapToDto(categoryRepository.findAll());
    }

    public CategoryResponse save(CategoryRequest categoryRequest) {
        Category category = CategoryMapper.mapToEntity(categoryRequest);
        categoryRepository.save(category);
        return CategoryMapper.mapToDto(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Category foundCategory = findById(id);
        foundCategory.setName(categoryRequest.name());

        categoryRepository.save(foundCategory);
        return CategoryMapper.mapToDto(foundCategory);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    protected Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
