package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/products/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductsPageService productsPageService;

    @GetMapping(params = {"query"})
    public ResponseEntity<PageProductsWithPromotionDTO> getSearchProductsByText(@RequestParam(value = "query", defaultValue = "puzzle") String text,
                                                                                @RequestBody PageProductsOptions pageProductsOptions) {
        return ResponseEntity.ok(productsPageService.getPageSearchProducts(text, pageProductsOptions));
    }
}
