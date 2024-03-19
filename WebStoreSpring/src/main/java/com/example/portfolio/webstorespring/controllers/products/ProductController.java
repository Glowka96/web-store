package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
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

    @GetMapping(value = "/admin/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<ProductWithProducerAndPromotionDTO> getProductById(@PathVariable(value = "productId") Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
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

    @DeleteMapping(value = "/admin/products/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable(value = "productId") Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
