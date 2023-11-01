package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.CategoryRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    public SubcategoryResponse getSubcategoryDtoById(Long id) {
        Subcategory foundSubcategory = findSubcategoryById(id);
        return subcategoryMapper.mapToDto(foundSubcategory);
    }

    public List<SubcategoryResponse> getAllSubcategory() {
        return subcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    public SubcategoryResponse saveSubcategory(Long categoryId,
                                               SubcategoryRequest subCategoryRequest) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory subcategory = subcategoryMapper.mapToEntity(subCategoryRequest);

        subcategory.setCategory(foundCategory);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.mapToDto(subcategory);
    }

    public SubcategoryResponse updateSubcategory(Long categoryId,
                                                 Long subCategoryId,
                                                 SubcategoryRequest subCategoryRequest) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory foundSubcategory = findSubcategoryById(subCategoryId);

        Subcategory subCategory = subcategoryMapper.mapToEntity(subCategoryRequest);
        setupUpdateSubcategory(foundCategory, foundSubcategory, subCategory);

        subcategoryRepository.save(subCategory);
        return subcategoryMapper.mapToDto(subCategory);
    }

    public void deleteSubcategoryById(Long subCategoryId) {
        Subcategory foundSubcategory = findSubcategoryById(subCategoryId);
        subcategoryRepository.deleteById(foundSubcategory.getId());
    }

    private Subcategory findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private void setupUpdateSubcategory(Category foundCategory,
                                        Subcategory foundSubcategory,
                                        Subcategory updatedSubcategory) {
        updatedSubcategory.setId(foundSubcategory.getId());
        updatedSubcategory.setCategory(foundCategory);
        if (updatedSubcategory.getName() == null) {
            updatedSubcategory.setName(foundSubcategory.getName());
        }
        if (updatedSubcategory.getProducts() == null) {
            updatedSubcategory.setProducts(foundSubcategory.getProducts());
        }
    }
}
