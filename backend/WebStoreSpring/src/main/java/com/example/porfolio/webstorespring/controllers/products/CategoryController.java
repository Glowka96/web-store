package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.CategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.CategoryResponse;
import com.example.porfolio.webstorespring.services.products.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategoryDto());
    }

    @GetMapping(value = "/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryDtoById(categoryId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.save(categoryRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                                           @Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(categoryService.update(categoryId, categoryRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}
