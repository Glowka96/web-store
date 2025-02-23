package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductQualityRequest;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/admin/products")
    public List<ProductResponse> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/products/{productId}")
    public ProductWithProducerAndPromotionDTO getProductById(@PathVariable("productId") Long productId) {
        return productService.getById(productId);
    }

    @PostMapping("/admin/subcategories/{subcategoryId}/producers/{producerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse saveProduct(@PathVariable("subcategoryId") Long subcategoryId,
                                       @PathVariable("producerId") Long producerId,
                                       @Valid @RequestBody ProductRequest request) {
        return productService.save(subcategoryId, producerId, request);
    }

    @PutMapping("/admin/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}")
    public ProductResponse updateProduct(@PathVariable("subcategoryId") Long subcategoryId,
                                         @PathVariable("producerId") Long producerId,
                                         @PathVariable("productId") Long productId,
                                         @Valid @RequestBody ProductRequest request) {
        return productService.update(subcategoryId, producerId, productId, request);
    }

    @PatchMapping("/admin/products")
    public ResponseMessageDTO updateProductQuantity(@Valid @RequestBody ProductQualityRequest request) {
        return productService.updateQuality(request);
    }

    @DeleteMapping("/admin/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }
}
