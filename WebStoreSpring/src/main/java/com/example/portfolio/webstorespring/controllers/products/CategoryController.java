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

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.saveCategory(categoryRequest);
    }

    @PutMapping(value = "/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryResponse updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                           @Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(categoryId, categoryRequest);
    }

    @DeleteMapping(value = "/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable(value = "categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }
}
