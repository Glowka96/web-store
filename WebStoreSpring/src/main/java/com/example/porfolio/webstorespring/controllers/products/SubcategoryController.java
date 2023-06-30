package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryResponse;
import com.example.porfolio.webstorespring.services.products.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/categories/")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping(value = "/subcategories/{subCategoryId}")
    public ResponseEntity<SubcategoryResponse> getSubcategoryById(@PathVariable("subCategoryId") Long id) {
        return ResponseEntity.ok(subcategoryService.getSubcategoryDtoById(id));
    }

    @GetMapping(value = "/subcategories")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategory() {
        return ResponseEntity.ok(subcategoryService.getAllSubcategoryResponse());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryResponse> saveSubcategory(@PathVariable("categoryId") Long id,
                                                              @Valid @RequestBody SubcategoryRequest subcategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.save(id, subcategoryRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryResponse> updateSubcategory(@PathVariable("categoryId") Long categoryId,
                                                                @PathVariable("subcategoryId") Long subcategoryId,
                                                                @Valid @RequestBody SubcategoryRequest subCategoryRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(subcategoryService.update(categoryId, subcategoryId, subCategoryRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubcategoryById(@PathVariable ("subcategoryId") Long id){
        subcategoryService.deleteSubcategoryById(id);
    }
}
