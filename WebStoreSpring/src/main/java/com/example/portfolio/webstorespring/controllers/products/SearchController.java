package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductsPageOptions;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductsPageService productsPageService;

    @GetMapping(params = {"query"})
    public PageProductsWithPromotionDTO getPageSearchProductsByText(@RequestParam(value = "query", defaultValue = "puzzle") String text,
                                                                    @Valid @RequestBody ProductsPageOptions productsPageOptions) {
        return productsPageService.getSearchProductsPage(text, productsPageOptions);
    }
}
