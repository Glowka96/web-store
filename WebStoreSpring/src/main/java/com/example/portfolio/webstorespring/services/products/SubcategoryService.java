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

    public List<SubcategoryResponse> getAll() {
        return subcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    Set<Subcategory> findAllByNames(Set<String> names){
        Set<Subcategory> subcategories = subcategoryRepository.findAllByNames(names);
        if (subcategories.isEmpty()) {
            throw new NotFoundSubcategoriesByNamesException();
        }
        return subcategories;
    }

    @Transactional
    public SubcategoryResponse save(Long categoryId,
                                    SubcategoryRequest subcategoryRequest) {
        Category foundCategory = categoryService.findById(categoryId);
        Subcategory subcategory = subcategoryMapper.mapToEntity(subcategoryRequest);

        subcategory.setCategory(foundCategory);
        subcategoryRepository.save(subcategory);
        return subcategoryMapper.mapToDto(subcategory);
    }

    @Transactional
    public SubcategoryResponse update(Long categoryId,
                                      Long subcategoryId,
                                      SubcategoryRequest subcategoryRequest) {
        Subcategory foundSubcategory = findById(subcategoryId);

        foundSubcategory.setName(subcategoryRequest.getName());
        foundSubcategory.setCategory(categoryService.findById(categoryId));

        subcategoryRepository.save(foundSubcategory);
        return subcategoryMapper.mapToDto(foundSubcategory);
    }

    public void deleteById(Long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }


    protected Subcategory findById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }
}
