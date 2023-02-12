package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.SubCategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubCategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    public SubCategoryDto getSubCategoryDtoById(Long id) {
        SubCategory foundSubCategory = findSubCategoryById(id);
        return subCategoryMapper.mapToDto(foundSubCategory);
    }

    public SubCategoryDto save(Long categoryId,
                               SubCategoryDto subCategoryDto) {
        Category foundCategory = findCategoryById(categoryId);
        SubCategory subCategory = subCategoryMapper.mapToEntity(subCategoryDto);

        subCategory.setCategory(foundCategory);
        subCategoryRepository.save(subCategory);
        return subCategoryMapper.mapToDto(subCategory);
    }

    public SubCategoryDto update(Long categoryId,
                                 Long subCategoryId,
                                 SubCategoryDto subCategoryDto) {
        Category foundCategory = findCategoryById(categoryId);
        SubCategory foundSubCategory = findSubCategoryById(subCategoryId);

        SubCategory subCategory = subCategoryMapper.mapToEntity(subCategoryDto);
        setupSubCategory(foundCategory, foundSubCategory, subCategory);

        subCategoryRepository.save(subCategory);
        return subCategoryMapper.mapToDto(subCategory);
    }

    public void deleteSubCategory(Long subCategoryId) {
        SubCategory foundSubCategory = findSubCategoryById(subCategoryId);
        subCategoryRepository.deleteById(foundSubCategory.getId());
    }

    private SubCategory findSubCategoryById(Long id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private void setupSubCategory(Category foundCategory,
                                  SubCategory foundSubCategory,
                                  SubCategory updatedSubCategory) {
        updatedSubCategory.setId(foundSubCategory.getId());
        updatedSubCategory.setCategory(foundCategory);
        if (updatedSubCategory.getName() == null) {
            updatedSubCategory.setName(foundSubCategory.getName());
        }
        if (updatedSubCategory.getProducts() == null) {
            updatedSubCategory.setProducts(foundSubCategory.getProducts());
        }
    }
}
