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

    private final SubCategoryRepository repository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryMapper categoryMapper;

    public SubCategoryDto findSubCategoryById(Long id){
        return mapper.subCategoryToSubCategoryDto(
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "SubCategory with id " + id + " not found")));
    }

    public SubCategoryDto save(Long id, SubCategoryDto subCategoryDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + id + " not found"));
        subCategoryDto.setCategoryDto(categoryMapper.categoryToCategoryDto(category));
        SubCategory saveSubCategory = repository.save(
                mapper.subCategoryDtoToSubCategory(subCategoryDto));
        return mapper.subCategoryToSubCategoryDto(saveSubCategory);
    }
}
