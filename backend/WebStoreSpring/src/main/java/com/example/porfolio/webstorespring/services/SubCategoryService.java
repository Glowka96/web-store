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

    public SubCategoryDto getSubCategoryById(Long id) {
        SubCategory foundSubCategory = findSubCategoryById(id);
        return mapper.mapToDto(foundSubCategory);
    }

    public SubCategoryDto save(Long id, SubCategoryDto subCategoryDto) {
        Category foundCategory = findCategoryById(id);
        subCategoryDto.setCategoryDto(categoryMapper.mapToDto(foundCategory));
        SubCategory saveSubCategory = repository.save(
                mapper.mapToEntity(subCategoryDto));
        return mapper.mapToDto(saveSubCategory);
    }

    private SubCategory findSubCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",id));
    }

}
