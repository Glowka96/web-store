package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.ProductPricePromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import com.example.portfolio.webstorespring.repositories.products.ProductPricePromotionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductPricePromotionService {

    private final ProductPricePromotionRepository promotionRepository;
    private final ProductService productService;
    private final ProductPricePromotionMapper promotionMapper;

    @Transactional
    public ProductPricePromotionResponse saveProductPricePromotion(@NotNull ProductPricePromotionRequest promotionRequest) {
        Product product = productService.findProductByIdWithPromotion(promotionRequest.getProductId());

        validateProduct(promotionRequest, product);

        ProductPricePromotion promotion = promotionMapper.mapToEntity(promotionRequest);
        promotion.setProduct(product);

        promotionRepository.save(promotion);
        return promotionMapper.mapToDto(promotion);
    }

    private void validateProduct(ProductPricePromotionRequest promotionRequest, Product product) {
        if (!product.getPricePromotions().isEmpty()) {
            throw new ProductHasAlreadyPromotionException();
        }

        if(product.getPrice().compareTo(promotionRequest.getPromotionPrice()) < 0) {
            throw new PromotionPriceGreaterThanBasePriceException();
        }
    }
}
