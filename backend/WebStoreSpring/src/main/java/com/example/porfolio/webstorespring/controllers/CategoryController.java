package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> findAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.save(categoryDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(value = "id") Long id,
                                                      @Valid @RequestBody CategoryDto categoryDto){
        /*if(!categoryService.findById(id).isPresent()){
            return ResponseEntity.badRequest().build();
        }*/
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(categoryService.update(id, categoryDto));
    }
}
