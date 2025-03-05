package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.PromotionMapper;
import com.example.portfolio.webstorespring.models.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.models.entity.products.Promotion;
import com.example.portfolio.webstorespring.repositories.products.PromotionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductService productService;

    @Transactional
    public PromotionResponse save(@NotNull PromotionRequest request) {
        log.info("Saving promotion from request: {}", request);
        log.debug("Fetching product for ID: {}", request.productId());
        Product product = productService.findWithPromotionById(request.productId());

        validateProduct(request, product);

        Promotion promotion = PromotionMapper.mapToEntity(request);
        promotion.setProduct(product);

        promotionRepository.save(promotion);
        log.info("Saved promotion.");
        return PromotionMapper.mapToDto(promotion);
    }

    private void validateProduct(PromotionRequest request, Product product) {
        log.debug("Validating if product has promotion.");
        if (!product.getPromotions().isEmpty()) {
            throw new ProductHasAlreadyPromotionException();
        }

        log.debug("Validating if product price is greater promotion price.");
        if(product.getPrice().compareTo(request.promotionPrice()) < 0) {
            throw new PromotionPriceGreaterThanBasePriceException();
        }
    }
}
