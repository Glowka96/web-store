package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.CategoryMapper;
import com.example.portfolio.webstorespring.models.dtos.products.requests.CategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.CategoryResponse;
import com.example.portfolio.webstorespring.models.entities.products.Category;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        log.info("Fetching all category.");
        return CategoryMapper.mapToResponse(categoryRepository.findAll());
    }

    public CategoryResponse save(CategoryRequest request) {
        log.info("Saving category from request: {}", request);
        Category category = CategoryMapper.mapToEntity(request);
        categoryRepository.save(category);
        log.info("Saved category.");
        return CategoryMapper.mapToResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        log.info("Updating category for ID: {}, from request: {}", id, request);
        Category foundCategory = findById(id);
        foundCategory.setName(request.name());

        categoryRepository.save(foundCategory);
        log.info("Updated category.");
        return CategoryMapper.mapToResponse(foundCategory);
    }

    public void deleteById(Long id) {
        log.info("Deleting category by ID: {}", id);
        categoryRepository.deleteById(id);
    }

    protected Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
