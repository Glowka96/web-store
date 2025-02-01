package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.services.products.PromotionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionResponse savePromotion(@NotNull @RequestBody PromotionRequest request) {
        return promotionService.save(request);
    }
}
