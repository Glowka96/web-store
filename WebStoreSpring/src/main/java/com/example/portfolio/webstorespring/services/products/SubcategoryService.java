package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.NotFoundSubcategoriesByNamesException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
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

    public List<SubcategoryResponse> getAll() {
        return SubcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    Set<Subcategory> findAllByNames(Set<String> names) {
        Set<Subcategory> subcategories = subcategoryRepository.findAllByNames(names);
        if (subcategories.isEmpty()) {
            throw new NotFoundSubcategoriesByNamesException();
        }
        return subcategories;
    }

    @Transactional
    public SubcategoryResponse save(Long categoryId,
                                    SubcategoryRequest subcategoryRequest) {
        Subcategory subcategory = Subcategory.builder()
                .name(subcategoryRequest.name())
                .category(categoryService.findById(categoryId))
                .build();
        subcategoryRepository.save(subcategory);
        return SubcategoryMapper.mapToDto(subcategory);
    }

    @Transactional
    public SubcategoryResponse update(Long categoryId,
                                      Long subcategoryId,
                                      SubcategoryRequest subcategoryRequest) {
        Subcategory foundSubcategory = findById(subcategoryId);

        foundSubcategory.setName(subcategoryRequest.name());
        foundSubcategory.setCategory(categoryService.findById(categoryId));

        subcategoryRepository.save(foundSubcategory);
        return SubcategoryMapper.mapToDto(foundSubcategory);
    }

    public void deleteById(Long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }


    protected Subcategory findById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }
}
