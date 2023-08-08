package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/products")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping(value = "/search/{text}", params = {"page", "size", "sort"})
    public ResponseEntity<List<ProductResponse>> getSearchProductsByText(@PathVariable("text") String text,
                                                                         @RequestParam("page") Integer page,
                                                                         @RequestParam("size") Integer size,
                                                                         @RequestParam("sort") String sort) {
        return ResponseEntity.ok(productService.getSearchProducts(text, page, size, sort));
    }

    @GetMapping(value = "/search/{text}/quantity")
    public ResponseEntity<Long> getQuantityOfSearchProducts(@PathVariable("text") String text) {
        return ResponseEntity.ok((productService.getAmountSearchProducts(text)));
    }
}
