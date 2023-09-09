package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/products/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping(params = {"text", "page", "size", "sort"})
    public ResponseEntity<List<ProductResponse>> getSearchProductsByText(@RequestParam(value = "text", defaultValue = "puzzle") String text,
                                                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                         @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return ResponseEntity.ok(productService.getSearchProducts(text, page, size, sort));
    }

    @GetMapping(value = "/quantity", params = {"text"})
    public ResponseEntity<Long> getQuantityOfSearchProducts(@RequestParam(value = "text", defaultValue = "puzzle") String text) {
        return ResponseEntity.ok((productService.getQuantityOfSearchProducts(text)));
    }
}
