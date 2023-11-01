package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.services.products.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping(value = "/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryDtoById(categoryId));
    }

    @PostMapping(value = "/admin/categories")
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(categoryRequest));
    }

    @PutMapping(value = "/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                                           @Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.accepted()
                .body(categoryService.updateCategory(categoryId, categoryRequest));
    }

    @DeleteMapping(value = "/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }
}
