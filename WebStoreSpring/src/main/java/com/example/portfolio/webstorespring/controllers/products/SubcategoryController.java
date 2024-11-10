package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.services.products.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/categories")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/subcategories")
    public List<SubcategoryResponse> getAllSubcategory() {
        return subcategoryService.getAll();
    }

    @PostMapping("/{categoryId}/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    public SubcategoryResponse saveSubcategory(@PathVariable("categoryId") Long id,
                                               @Valid @RequestBody SubcategoryRequest subcategoryRequest) {
        return subcategoryService.save(id, subcategoryRequest);
    }

    @PutMapping("/{categoryId}/subcategories/{subcategoryId}")
    public SubcategoryResponse updateSubcategory(@PathVariable("categoryId") Long categoryId,
                                                 @PathVariable("subcategoryId") Long subcategoryId,
                                                 @Valid @RequestBody SubcategoryRequest subCategoryRequest) {
        return subcategoryService.update(categoryId, subcategoryId, subCategoryRequest);
    }

    @DeleteMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubcategoryById(@PathVariable("subcategoryId") Long id) {
        subcategoryService.deleteById(id);
    }
}
