package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.services.products.ProductTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @GetMapping("/product-types")
    public ResponseEntity<List<ProductTypeResponse>> getAllProductType() {
        return ResponseEntity.ok(productTypeService.getAllProductType());
    }

    @PostMapping(value = "/admin/product-types")
    public ResponseEntity<ProductTypeResponse> saveProductType(@Valid @RequestBody ProductTypeRequest productTypeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productTypeService.saveProductType(productTypeRequest));
    }

    @PutMapping(value = "/admin/product-types/{productTypeId}")
    public ResponseEntity<ProductTypeResponse> updateProductType(@PathVariable("productTypeId") Long productTypeId,
                                                                 @Valid @RequestBody ProductTypeRequest productTypeRequest) {
        return ResponseEntity.accepted()
                .body(productTypeService.updateProductType(productTypeId, productTypeRequest));
    }

    @DeleteMapping(value = "/admin/product-types/{productTypeId}")
    public ResponseEntity<Void> deleteProductTypeById(@PathVariable("productTypeId") Long productTypeId) {
        productTypeService.deleteProductTypeById(productTypeId);
        return ResponseEntity.noContent().build();
    }
}
