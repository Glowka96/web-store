package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.services.products.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin("*")
@RequestMapping(value = "api/v1/subcategories/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "products/types")
    public ResponseEntity<ProductType[]> getAllProductTyp() {
        return ResponseEntity.ok(ProductType.values());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/{subcategoryId}/products", params = {"page", "size"})
    public ResponseEntity<List<ProductResponse>> getAllProductsBySubCategoryId(@PathVariable("subcategoryId") Long subcategoryId,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("size") Integer size) {
        return ResponseEntity.ok(productService.getAllProductsBySubcategoryId(subcategoryId, page, size));
    }

    @GetMapping(value = "/{subcategoryId}/products", params = {"page", "size", "sort"})
    public ResponseEntity<List<ProductResponse>> getAllProductsBySubCategoryId(@PathVariable("subcategoryId") Long subcategoryId,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("size") Integer size,
                                                                              @RequestParam("sort") String sort) {
        return ResponseEntity.ok(productService.getAllProductsBySubcategoryId(subcategoryId, page, size, sort));
    }

    @GetMapping(value = "/{subcategoryId}/products/amount")
    public ResponseEntity<Long> getQuantityOfProductsBySubcategoryId(@PathVariable("subcategoryId") Long subcategoryId) {
        return ResponseEntity.ok(productService.getQuantityOfProductsBySubcategoryId(subcategoryId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{subcategoryId}/producers/{producerId}/products")
    public ResponseEntity<ProductResponse> saveProduct(@PathVariable("subcategoryId") Long subcategoryId,
                                                      @PathVariable("producerId") Long producerId,
                                                      @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(subcategoryId, producerId, productRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{subcategoryId}/producers/{producerId}/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long subcategoryId,
                                                        @PathVariable Long producerId,
                                                        @PathVariable Long productId,
                                                        @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productService.updateProduct(subcategoryId, producerId, productId, productRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }
}