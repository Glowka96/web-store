package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.PromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.example.portfolio.webstorespring.repositories.products.PromotionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductService productService;

    @Transactional
    public PromotionResponse save(@NotNull PromotionRequest promotionRequest) {
        Product product = productService.findWithPromotionById(promotionRequest.productId());

        validateProduct(promotionRequest, product);

        Promotion promotion = PromotionMapper.mapToEntity(promotionRequest);
        promotion.setProduct(product);

        promotionRepository.save(promotion);
        return PromotionMapper.mapToDto(promotion);
    }

    private void validateProduct(PromotionRequest promotionRequest, Product product) {
        if (!product.getPromotions().isEmpty()) {
            throw new ProductHasAlreadyPromotionException();
        }

        if(product.getPrice().compareTo(promotionRequest.promotionPrice()) < 0) {
            throw new PromotionPriceGreaterThanBasePriceException();
        }
    }
}
