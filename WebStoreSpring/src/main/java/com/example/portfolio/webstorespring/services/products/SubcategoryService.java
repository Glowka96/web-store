package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.NotFoundSubcategoriesByNamesException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryService categoryService;
    private final SubcategoryMapper subcategoryMapper;

    public List<SubcategoryResponse> getAllSubcategory() {
        return subcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    Set<Subcategory> findAllSubcategoryByNames(Set<String> names){
        Set<Subcategory> subcategories = subcategoryRepository.findAllByNames(names);
        if (subcategories.isEmpty()) {
            throw new NotFoundSubcategoriesByNamesException();
        }
        return subcategories;
    }

    @Transactional
    public SubcategoryResponse saveSubcategory(Long categoryId,
                                               SubcategoryRequest subcategoryRequest) {
        Category foundCategory = categoryService.findCategoryById(categoryId);
        Subcategory subcategory = subcategoryMapper.mapToEntity(subcategoryRequest);

        subcategory.setCategory(foundCategory);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.mapToDto(subcategory);
    }

    @Transactional
    public SubcategoryResponse updateSubcategory(Long categoryId,
                                                 Long subcategoryId,
                                                 SubcategoryRequest subcategoryRequest) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);

        foundSubcategory.setName(subcategoryRequest.getName());
        foundSubcategory.setCategory(categoryService.findCategoryById(categoryId));

        subcategoryRepository.save(foundSubcategory);
        return subcategoryMapper.mapToDto(foundSubcategory);
    }

    public void deleteSubcategoryById(Long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }


    protected Subcategory findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }
}
