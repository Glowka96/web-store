package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductService;
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

    private final ProductService productService;

    @GetMapping(params = {"text", "page", "size", "sort", "direction"})
    public ResponseEntity<PageProductsWithPromotionDTO> getSearchProductsByText(@RequestParam(value = "text", defaultValue = "puzzle") String text,
                                                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                                @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
                                                                                @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(productService.getPageSearchProducts(text, page, size, sort, sortDirection));
    }
}
