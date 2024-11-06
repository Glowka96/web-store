package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
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
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public ProductWithProducerAndPromotionDTO getProductById(@PathVariable(value = "productId") Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping("/admin/subcategories/{subcategoryId}/producers/{producerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse saveProduct(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                       @PathVariable(value = "producerId") Long producerId,
                                       @Valid @RequestBody ProductRequest productRequest) {
        return productService.saveProduct(subcategoryId, producerId, productRequest);
    }

    @PutMapping("/admin/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}")
    public ProductResponse updateProduct(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                         @PathVariable(value = "producerId") Long producerId,
                                         @PathVariable(value = "productId") Long productId,
                                         @Valid @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(subcategoryId, producerId, productId, productRequest);
    }

    @DeleteMapping("/admin/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable(value = "productId") Long id) {
        productService.deleteProductById(id);
    }
}
