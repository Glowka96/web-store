package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "api/v1/products")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping(value = "/search/{text}", params = {"page", "size", "sort"})
    public ResponseEntity<List<ProductDto>> getSearchProductsByText(@PathVariable("text") String text,
                                                              @RequestParam("page") Integer page,
                                                              @RequestParam("size") Integer size,
                                                              @RequestParam("sort") String sort) {
        return ResponseEntity.ok(productService.getSearchProducts(text, page, size, sort));
    }

    @GetMapping(value = "/search/{text}/amount")
    public ResponseEntity<Long> getAmountSearchProducts(@PathVariable("text") String text) {
        return ResponseEntity.ok((productService.getAmountSearchProducts(text)));
    }
}
