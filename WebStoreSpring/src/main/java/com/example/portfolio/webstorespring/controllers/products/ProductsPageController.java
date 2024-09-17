package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class ProductsPageController {

    private final ProductsPageService productsPageService;

    @GetMapping(value = "/subcategories/{subcategoryId}/products")
    public ResponseEntity<PageProductsWithPromotionDTO> getPageProductsBySubcategoryId(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                                                       @Valid @RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPageProductsBySubcategoryId(subcategoryId, pageProductsOptions));
    }

    @GetMapping(value = "/products/promotions")
    public ResponseEntity<PageProductsWithPromotionDTO> getPagePromotionProducts(@Valid @RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPagePromotionProduct(pageProductsOptions));
    }

    @GetMapping(value = "/products/news")
    public ResponseEntity<PageProductsWithPromotionDTO> getPageNewProducts(@Valid @RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPageNewProducts(pageProductsOptions));
    }
}
