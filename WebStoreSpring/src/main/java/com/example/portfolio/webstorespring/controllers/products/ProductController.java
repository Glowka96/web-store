package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.enums.ProductType;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/admin/products/types")
    public ResponseEntity<ProductType[]> getAllProductTyp() {
        return ResponseEntity.ok(ProductType.values());
    }

    @GetMapping(value = "/admin/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/subcategories/{subcategoryId}/products", params = {"page", "size", "sort"})
    public ResponseEntity<List<ProductResponse>> getAllProductsBySubCategoryId(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                              @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                              @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return ResponseEntity.ok(productService.getAllProductsBySubcategoryId(subcategoryId, page, size, sort));
    }

    @GetMapping(value = "/subcategories/{subcategoryId}/products/quantity")
    public ResponseEntity<Long> getQuantityOfProductsBySubcategoryId(@PathVariable(value = "subcategoryId") Long subcategoryId) {
        return ResponseEntity.ok(productService.getQuantityOfProductsBySubcategoryId(subcategoryId));
    }

    @PostMapping(value = "/admin/subcategories/{subcategoryId}/producers/{producerId}/products")
    public ResponseEntity<ProductResponse> saveProduct(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                      @PathVariable(value = "producerId") Long producerId,
                                                      @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(subcategoryId, producerId, productRequest));
    }

    @PutMapping(value = "/admin/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                        @PathVariable(value = "producerId") Long producerId,
                                                        @PathVariable(value = "productId") Long productId,
                                                        @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.accepted()
                .body(productService.updateProduct(subcategoryId, producerId, productId, productRequest));
    }

    @DeleteMapping(value = "/admin/subcategories/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProductById(@PathVariable(value = "productId") Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
