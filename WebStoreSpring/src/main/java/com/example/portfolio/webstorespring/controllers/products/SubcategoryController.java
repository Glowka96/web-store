package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.SubcategoryResponse;
import com.example.portfolio.webstorespring.services.products.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping(value = "/categories/subcategories/{subCategoryId}")
    public ResponseEntity<SubcategoryResponse> getSubcategoryById(@PathVariable("subCategoryId") Long id) {
        return ResponseEntity.ok(subcategoryService.getSubcategoryDtoById(id));
    }

    @GetMapping(value = "/categories/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategory() {
        return ResponseEntity.ok(subcategoryService.getAllSubcategory());
    }

    @PostMapping(value = "/admin/categories/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryResponse> saveSubcategory(@PathVariable("categoryId") Long id,
                                                              @Valid @RequestBody SubcategoryRequest subcategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.saveSubcategory(id, subcategoryRequest));
    }

    @PutMapping(value = "/admin/categories/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryResponse> updateSubcategory(@PathVariable("categoryId") Long categoryId,
                                                                @PathVariable("subcategoryId") Long subcategoryId,
                                                                @Valid @RequestBody SubcategoryRequest subCategoryRequest) {
        return ResponseEntity.accepted()
                .body(subcategoryService.updateSubcategory(categoryId, subcategoryId, subCategoryRequest));
    }

    @DeleteMapping(value = "/admin/categories/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSubcategoryById(@PathVariable ("subcategoryId") Long id){
        subcategoryService.deleteSubcategoryById(id);
        return ResponseEntity.noContent().build();

    }
}
