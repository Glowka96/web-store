package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("api/v1/subcategories/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/{subcategoryId}/products", params = {"page", "size"})
    public ResponseEntity<List<ProductDto>> getAllProductsBySubCategoryId(@PathVariable("subcategoryId") Long subcategoryId,
                                                                          @RequestParam("page") Integer page,
                                                                          @RequestParam("size") Integer size) {
        return ResponseEntity.ok(productService.getAllProductsBySubCategoryId(subcategoryId, page, size));
    }

    @GetMapping(value = "/{subcategoryId}/products", params = {"page", "size", "sort"})
    public ResponseEntity<List<ProductDto>> getAllProductsBySubCategoryId(@PathVariable("subcategoryId") Long subcategoryId,
                                                                          @RequestParam("page") Integer page,
                                                                          @RequestParam("size") Integer size,
                                                                          @RequestParam("sort") String sort) {
        return ResponseEntity.ok(productService.getAllProductsBySubCategoryId(subcategoryId, page, size, sort));
    }

    @PostMapping("/{subcategoryId}/producers/{producerId}/products")
    public ResponseEntity<ProductDto> saveProduct(@PathVariable("subcategoryId") Long subcategoryId,
                                                  @PathVariable("producerId") Long producerId,
                                                  @Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(subcategoryId, producerId, productDto));
    }

    @PutMapping("/{subcategoryId}/producers/{producerId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long subcategoryId,
                                                    @PathVariable Long producerId,
                                                    @PathVariable Long productId,
                                                    @Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productService.updateProduct(subcategoryId, producerId, productId, productDto));
    }

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable("productId") Long id) {
        productService.deleteById(id);
    }
}
