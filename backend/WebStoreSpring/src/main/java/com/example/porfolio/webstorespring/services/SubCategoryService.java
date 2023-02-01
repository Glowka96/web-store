package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.CategoryMapper;
import com.example.porfolio.webstorespring.mappers.SubCategoryMapper;
import com.example.porfolio.webstorespring.model.dto.products.SubCategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;
    private final CategoryMapper categoryMapper;

    public SubCategoryDto getSubCategoryById(Long id) {
        SubCategory foundSubCategory = findSubCategoryById(id);
        return subCategoryMapper.mapToDto(foundSubCategory);
    }

    public SubCategoryDto getSubCategoryByName(String name) {
        SubCategory foundSubCategory = findSubCategoryByName(name);
        return subCategoryMapper.mapToDto(foundSubCategory);
    }

    public SubCategoryDto saveSubCategory(String categoryName,
                                          SubCategoryDto subCategoryDto) {
        Category foundCategory = findCategoryByName(categoryName);

        subCategoryDto.setCategoryDto(categoryMapper.mapToDto(foundCategory));

        SubCategory saveSubCategory = subCategoryRepository.save(
                subCategoryMapper.mapToEntity(subCategoryDto));
        return subCategoryMapper.mapToDto(saveSubCategory);
    }

    public SubCategoryDto updateSubCategory(String categoryName,
                                            String subCategoryName,
                                            SubCategoryDto subCategoryDto) {
        Category foundCategory = findCategoryByName(categoryName);
        SubCategory foundSubCategory = findSubCategoryByName(subCategoryName);
        SubCategory subCategory = subCategoryMapper.mapToEntity(subCategoryDto);

        subCategory.setId(foundSubCategory.getId());
        subCategory.setCategory(foundCategory);

        subCategoryRepository.save(subCategory);
        return subCategoryMapper.mapToDto(subCategory);
    }

    public void deleteSubCategory(String subCategoryName) {
        SubCategory foundSubCategory = findSubCategoryByName(subCategoryName);
        subCategoryRepository.deleteByIdName(foundSubCategory.getName());
    }

    private SubCategory findSubCategoryById(Long id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
    }

    private SubCategory findSubCategoryByName(String name) {
        return subCategoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "name", name));
    }

    private Category findCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }
}
