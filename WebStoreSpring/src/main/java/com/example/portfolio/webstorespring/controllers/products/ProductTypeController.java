package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.services.products.ProductTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @GetMapping("/product-types")
    public List<ProductTypeResponse> getAllProductType() {
        return productTypeService.getAll();
    }

    @PostMapping("/admin/product-types")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTypeResponse saveProductType(@Valid @RequestBody ProductTypeRequest request) {
        return productTypeService.save(request);
    }

    @PutMapping("/admin/product-types/{productTypeId}")
    public ProductTypeResponse updateProductType(@PathVariable("productTypeId") Long productTypeId,
                                                 @Valid @RequestBody ProductTypeRequest request) {
        return productTypeService.update(productTypeId, request);
    }

    @DeleteMapping("/admin/product-types/{productTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductTypeById(@PathVariable("productTypeId") Long productTypeId) {
        productTypeService.deleteById(productTypeId);
    }
}
