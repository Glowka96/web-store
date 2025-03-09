package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.CategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.CategoryResponse;
import com.example.portfolio.webstorespring.services.products.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse saveCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public CategoryResponse updateCategory(@PathVariable("categoryId") Long categoryId,
                                           @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(categoryId, request);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}
