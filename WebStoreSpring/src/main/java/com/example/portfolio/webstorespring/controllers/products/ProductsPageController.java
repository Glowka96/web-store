package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class ProductsPageController {

    private final ProductsPageService productsPageService;

    @GetMapping(value = "/subcategories/{subcategoryId}/products")
    public ResponseEntity<PageProductsWithPromotionDTO> getPageProductsBySubCategoryId(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                                                       @RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPageProductsBySubcategoryId(subcategoryId, pageProductsOptions));
    }

    @GetMapping(value = "/products/promotions")
    public ResponseEntity<PageProductsWithPromotionDTO> getPagePromotionProduct(@RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPagePromotionProduct(pageProductsOptions));
    }

    @GetMapping(value = "/products/news")
    public ResponseEntity<PageProductsWithPromotionDTO> getPageNewProduct(@RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPageNewProduct(pageProductsOptions));
    }

}
