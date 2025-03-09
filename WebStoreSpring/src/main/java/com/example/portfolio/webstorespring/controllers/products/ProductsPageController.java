package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductsPageOptions;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1")
@RequiredArgsConstructor
public class ProductsPageController {

    private final ProductsPageService productsPageService;

    @GetMapping("/subcategories/{subcategoryId}/products")
    public PageProductsWithPromotionDTO getPageProductsBySubcategoryId(@PathVariable("subcategoryId") Long subcategoryId,
                                                                       @Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return productsPageService.getProductsPageBySubcategoryId(subcategoryId, productsPageOptions);
    }

    @GetMapping("/products/promotions")
    public PageProductsWithPromotionDTO getPagePromotionProducts(@Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return productsPageService.getPromotionProductsPage(productsPageOptions);
    }

    @GetMapping("/products/news")
    public PageProductsWithPromotionDTO getPageNewProducts(@Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return productsPageService.getNewProductsPage(productsPageOptions);
    }
}
