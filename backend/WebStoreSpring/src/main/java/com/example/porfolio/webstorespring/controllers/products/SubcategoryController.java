package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.services.products.SubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/v1/categories/")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping(value = "/subcategories/{subCategoryId}")
    public ResponseEntity<SubcategoryDto> getSubcategoryById(@PathVariable("subCategoryId") Long id) {
        return ResponseEntity.ok(subcategoryService.getSubcategoryDtoById(id));
    }

    @GetMapping(value = "/subcategories")
    public ResponseEntity<List<SubcategoryDto>> getAllSubcategory() {
        return ResponseEntity.ok(subcategoryService.getAllSubcategoryDto());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryDto> saveSubcategory(@PathVariable("categoryId") Long id,
                                                          @Valid @RequestBody SubcategoryDto subcategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoryService.save(id, subcategoryDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{categoryId}/subcategories/{subcategoryId}")
    public ResponseEntity<SubcategoryDto> updateSubcategory(@PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("subcategoryId") Long subcategoryId,
                                                            @Valid @RequestBody SubcategoryDto subCategoryDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(subcategoryService.update(categoryId, subcategoryId, subCategoryDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubcategoryById(@PathVariable ("subcategoryId") Long id){
        subcategoryService.deleteSubcategoryById(id);
    }
}
