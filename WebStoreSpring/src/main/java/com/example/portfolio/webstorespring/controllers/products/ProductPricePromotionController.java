package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.services.products.ProductPricePromotionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/v1/admin/products/promotions")
@RequiredArgsConstructor
public class ProductPricePromotionController {

    private final ProductPricePromotionService promotionService;

    @PostMapping
    public ResponseEntity<ProductPricePromotionResponse> saveProductPricePromotion(@NotNull @RequestBody ProductPricePromotionRequest promotionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.saveProductPricePromotion(promotionRequest));
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteProductPricePromotion(@NotNull @Param("id") Long id) {
        promotionService.deleteProductPricePromotionById(id);
        return ResponseEntity.noContent().build();
    }
}
