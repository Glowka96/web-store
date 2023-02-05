package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.SubCategoryDto;
import com.example.porfolio.webstorespring.services.SubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/categories/")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/subcategories/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> getSubCategoryByName(@PathVariable("subCategoryId") Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryDtoById(id));
    }

    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubCategoryDto> saveSubCategory(@PathVariable("categoryId") Long id,
                                                          @Valid @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subCategoryService.save(id, subCategoryDto));
    }

    @PutMapping("/{categoryId}/subcategories/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("subCategoryId") Long subCategoryId,
                                                            @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(subCategoryService.update(categoryId, subCategoryId, subCategoryDto));
    }

    @DeleteMapping("/subcategories/{subCategoryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSubCategoryByName(@PathVariable ("subCategoryId") Long id){
        subCategoryService.deleteSubCategory(id);
    }
}
