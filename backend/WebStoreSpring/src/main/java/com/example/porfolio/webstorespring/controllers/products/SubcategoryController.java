package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.services.products.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/categories/")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/subcategories/{subCategoryId}")
    public ResponseEntity<SubcategoryDto> getSubcategoryById(@PathVariable("subCategoryId") Long id) {
        return ResponseEntity.ok(subcategoryService.getSubcategoryDtoById(id));
    }

    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryDto> saveSubcategory(@PathVariable("categoryId") Long id,
                                                          @Valid @RequestBody SubcategoryDto subcategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.save(id, subcategoryDto));
    }

    @PutMapping("/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryDto> updateSubcategory(@PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("subcategoryId") Long subcategoryId,
                                                            @Valid @RequestBody SubcategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(subcategoryService.update(categoryId, subcategoryId, subCategoryDto));
    }

    @DeleteMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubcategoryById(@PathVariable ("subcategoryId") Long id){
        subcategoryService.deleteSubcategoryById(id);
    }
}
