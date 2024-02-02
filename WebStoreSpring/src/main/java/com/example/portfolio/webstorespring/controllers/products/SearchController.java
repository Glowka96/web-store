package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/products/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductsPageService productsPageService;

    @GetMapping(params = {"query", "page", "size", "sort", "direction"})
    public ResponseEntity<PageProductsWithPromotionDTO> getSearchProductsByText(@RequestParam(value = "query", defaultValue = "puzzle") String text,
                                                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                                @RequestParam(name = "sort", required = false, defaultValue = "id") SortByType sort,
                                                                                @RequestParam(name = "direction", required = false, defaultValue = "asc") SortDirectionType sortDirection) {
        return ResponseEntity.ok(productsPageService.getPageSearchProducts(text, page, size, sort, sortDirection));
    }
}
