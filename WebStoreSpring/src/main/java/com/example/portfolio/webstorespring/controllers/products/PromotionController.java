package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequesst;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.services.products.PromotionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/products/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping()
    public ResponseEntity<PromotionResponse> savePromotion(@NotNull @RequestBody PromotionRequesst promotionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.savePromotion(promotionRequest));
    }
}
