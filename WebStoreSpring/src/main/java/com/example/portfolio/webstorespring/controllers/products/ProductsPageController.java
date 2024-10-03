package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductsPageOptions;
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
                                                                                       @Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return ResponseEntity.ok(productsPageService.getProductsPageBySubcategoryId(subcategoryId, productsPageOptions));
    }

    @GetMapping(value = "/products/promotions")
    public ResponseEntity<PageProductsWithPromotionDTO> getPagePromotionProducts(@Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return ResponseEntity.ok(productsPageService.getPromotionProductsPage(productsPageOptions));
    }

    @GetMapping(value = "/products/news")
    public ResponseEntity<PageProductsWithPromotionDTO> getPageNewProducts(@Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return ResponseEntity.ok(productsPageService.getNewProductsPage(productsPageOptions));
    }
}
