package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    public SubcategoryDto getSubcategoryDtoById(Long id) {
        Subcategory foundSubcategory = findSubcategoryById(id);
        return subcategoryMapper.mapToDto(foundSubcategory);
    }

    public SubcategoryDto save(Long categoryId,
                               SubcategoryDto subCategoryDto) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory subcategory = subcategoryMapper.mapToEntity(subCategoryDto);

        subcategory.setCategory(foundCategory);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.mapToDto(subcategory);
    }

    public SubcategoryDto update(Long categoryId,
                                 Long subCategoryId,
                                 SubcategoryDto subCategoryDto) {
        Category foundCategory = findCategoryById(categoryId);
        Subcategory foundSubcategory = findSubcategoryById(subCategoryId);

        Subcategory subCategory = subcategoryMapper.mapToEntity(subCategoryDto);
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
