package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.NotFoundSubcategoriesByNamesException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.SubcategoryMapper;
import com.example.portfolio.webstorespring.models.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.models.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryService categoryService;

    public List<SubcategoryResponse> getAll() {
        log.info("Fetching all subcategory.");
        return SubcategoryMapper.mapToDto(subcategoryRepository.findAll());
    }

    Set<Subcategory> findAllByNames(Set<String> names) {
        log.debug("Finding subcategory by names.");
        Set<Subcategory> subcategories = subcategoryRepository.findAllByNames(names);
        if (subcategories.isEmpty()) {
            throw new NotFoundSubcategoriesByNamesException();
        }
        log.debug("Returning subcategories.");
        return subcategories;
    }

    @Transactional
    public SubcategoryResponse save(Long categoryId,
                                    SubcategoryRequest request) {
        log.info("Saving subcategory from request: {}", request);
        Subcategory subcategory = SubcategoryMapper.mapToEntity(
                request,
                categoryService.findById(categoryId)
        );
        subcategoryRepository.save(subcategory);
        log.info("Saved subcategory.");
        return SubcategoryMapper.mapToDto(subcategory);
    }

    @Transactional
    public SubcategoryResponse update(Long categoryId,
                                      Long subcategoryId,
                                      SubcategoryRequest request) {
        log.info("Updating subcategory for ID: {}, form request: {}", subcategoryId, request);
        Subcategory foundSubcategory = findById(subcategoryId);

        foundSubcategory.setName(request.name());
        foundSubcategory.setCategory(categoryService.findById(categoryId));

        subcategoryRepository.save(foundSubcategory);
        log.info("Updated subcategory.");
        return SubcategoryMapper.mapToDto(foundSubcategory);
    }

    public void deleteById(Long id) {
        log.info("Deleting subcategory for ID: {}", id);
        subcategoryRepository.deleteById(id);
    }


    protected Subcategory findById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }
}
