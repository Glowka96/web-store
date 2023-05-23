package com.example.porfolio.webstorespring.services.products;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.products.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    public SubcategoryRequest getSubcategoryDtoById(Long id) {
        Subcategory foundSubcategory = findSubcategoryById(id);
        return subcategoryMapper.mapToDto(foundSubcategory);
    }

    public List<SubcategoryRequest> getAllSubcategoryDto() {
        return subcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    public SubcategoryRequest save(Long categoryId,
                                   SubcategoryRequest subCategoryRequest) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory subcategory = subcategoryMapper.mapToEntity(subCategoryRequest);

        subcategory.setCategory(foundCategory);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.mapToDto(subcategory);
    }

    public SubcategoryRequest update(Long categoryId,
                                     Long subCategoryId,
                                     SubcategoryRequest subCategoryRequest) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory foundSubcategory = findSubcategoryById(subCategoryId);

        Subcategory subCategory = subcategoryMapper.mapToEntity(subCategoryRequest);
        setupSubcategory(foundCategory, foundSubcategory, subCategory);

        subcategoryRepository.save(subCategory);
        return subcategoryMapper.mapToDto(subCategory);
    }

    public void deleteSubcategoryById(Long subCategoryId) {
        Subcategory foundSubcategory = findSubcategoryById(subCategoryId);
        subcategoryRepository.deleteById(foundSubcategory.getId());
    }

    private Subcategory findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private void setupSubcategory(Category foundCategory,
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
