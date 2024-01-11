package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
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

    @GetMapping(value = "/subcategories/{subcategoryId}/products", params = {"page", "size", "sort", "direction"})
    public ResponseEntity<PageProductsWithPromotionDTO> getPageProductsBySubCategoryId(@PathVariable(value = "subcategoryId") Long subcategoryId,
                                                                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                       @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                                       @RequestParam(name = "sort", required = false, defaultValue = "id") SortByType sort,
                                                                                       @RequestParam(name = "direction", required = false, defaultValue = "asc") SortDirectionType sortDirection) {
        return ResponseEntity.ok(productsPageService.getPageProductsBySubcategoryId(subcategoryId, page, size, sort, sortDirection));
    }

    @GetMapping(value = "/promotions/products", params = {"page", "size", "sort", "direction"})
    public ResponseEntity<PageProductsWithPromotionDTO> getPagePromotionProduct(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                                @RequestParam(name = "sort", required = false, defaultValue = "id") SortByType sort,
                                                                                @RequestParam(name = "direction", required = false, defaultValue = "asc") SortDirectionType sortDirection) {
        return ResponseEntity.ok(productsPageService.getPagePromotionProduct(page, size, sort, sortDirection));
    }

    @GetMapping(value = "/new-products", params = {"page", "size", "sort", "direction"})
    public ResponseEntity<PageProductsWithPromotionDTO> getPageNewProduct(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                          @RequestParam(name = "size", defaultValue = "12") Integer size,
                                                                          @RequestParam(name = "sort", required = false) SortByType sort,
                                                                          @RequestParam(name = "direction", required = false) SortDirectionType sortDirection) {
        return ResponseEntity.ok(productsPageService.getPageNewProduct(page, size, sort, sortDirection));
    }

}
