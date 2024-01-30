package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductPricePromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import com.example.portfolio.webstorespring.repositories.products.ProductPricePromotionRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductPricePromotionService {

    private final ProductPricePromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final ProductPricePromotionMapper promotionMapper;

    @Transactional
    public ProductPricePromotionResponse saveProductPricePromotion(@NotNull ProductPricePromotionRequest promotionRequest) {
        Product product = findProductById(promotionRequest.getProductId());

        if(!product.getPricePromotions().isEmpty()) {
            throw new ProductHasAlreadyPromotionException();
        }

        if(product.getPrice().compareTo(promotionRequest.getPromotionPrice()) < 0) {
            throw new PromotionPriceGreaterThanBasePriceException();
        }

        ProductPricePromotion promotion = promotionMapper.mapToEntity(promotionRequest);
        promotion.setProduct(product);

        promotionRepository.save(promotion);
        return promotionMapper.mapToDto(promotion);
    }

    @Transactional
    public void deleteProductPricePromotionById(@NotNull Long id) {
        ProductPricePromotion promotion = findProductPricePromotionById(id);
        promotionRepository.delete(promotion);
    }

    private Product findProductById(Long id) {
        return productRepository.findProductsByIdWithPromotion(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private ProductPricePromotion findProductPricePromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductPricePromotion", "id", id));
    }

}
