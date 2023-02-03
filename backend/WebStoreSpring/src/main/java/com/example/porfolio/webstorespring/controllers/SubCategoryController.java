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

    @GetMapping("/subcategories/{subCategoryName}")
    public ResponseEntity<SubCategoryDto> getSubCategoryByName(@PathVariable("subCategoryName") String name) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryDtoByName(name));
    }

    @PostMapping("/{categoryName}/subcategories")
    public ResponseEntity<SubCategoryDto> saveSubCategory(@PathVariable("categoryName") String name,
                                                          @Valid @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subCategoryService.save(name, subCategoryDto));
    }

    @PutMapping("/{categoryName}/subcategories/{subCategoryName}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@PathVariable("categoryName") String categoryName,
                                                            @PathVariable("subCategoryName") String subCategoryName,
                                                            @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(subCategoryService.update(categoryName, subCategoryName, subCategoryDto));
    }

    @DeleteMapping("/subcategories/{subCategoryName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSubCategoryByName(@PathVariable ("subCategoryName") String subCategoryName){
        subCategoryService.deleteSubCategory(subCategoryName);
    }
}
