package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductAvailableEvent;
import com.example.portfolio.webstorespring.models.dtos.products.ProductNameView;
import com.example.portfolio.webstorespring.models.dtos.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductQualityRequest;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductResponse;
import com.example.portfolio.webstorespring.models.entities.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerService producerService;
    private final SubcategoryService subcategoryService;
    private final ProductTypeService productTypeService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock = Clock.systemUTC();

    public ProductWithProducerAndPromotionDTO getById(Long id) {
        log.info("Fetching product for ID: {}", id);
        ProductWithProducerAndPromotionDTO product = productRepository.findById(id, getLocalDataTime30DaysAgo());
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        return product;
    }

    public ProductNameView getNameById(Long id) {
        log.info("Fetching product name for ID: {}", id);
        return productRepository.findNameById(id);
    }

    public List<ProductResponse> getAll() {
        log.info("Fetching all products.");
        return ProductMapper.mapToDto(productRepository.findAll());
    }

    @Transactional
    public ProductResponse save(Long subcategoryId, Long producerId, ProductRequest request) {
        log.info("Saving product from request: {}", request);
        Product product = ProductMapper.mapToEntity(request);

        log.debug("Finding others entities and setting them.");
        product.setSubcategory(subcategoryService.findById(subcategoryId));
        product.setProducer(producerService.findById(producerId));
        product.setType(productTypeService.findById(request.productTypeId()));

        productRepository.save(product);
        log.info("Saved product.");
        return ProductMapper.mapToDto(product);
    }

    @Transactional
    public ProductResponse update(Long subcategoryId,
                                  Long producerId,
                                  Long productId,
                                  ProductRequest request) {
        log.info("Updating product for ID: {}, from request: {}", productId, request);
        Product foundProduct = findById(productId);
        Product product = ProductMapper.mapToEntity(request);

        log.debug("Finding others entities. Setting found product fields.");
        foundProduct.setSubcategory(subcategoryService.findById(subcategoryId));
        foundProduct.setProducer(producerService.findById(producerId));
        foundProduct.setType(productTypeService.findById(request.productTypeId()));
        foundProduct.setName(product.getName());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setQuantity(product.getQuantity());
        foundProduct.setImageUrl(product.getImageUrl());

        productRepository.save(foundProduct);
        log.debug("Saved product");
        return ProductMapper.mapToDto(foundProduct);
    }

    @Transactional
    public ResponseMessageDTO updateQuality(ProductQualityRequest request) {
        log.info("Fetching product for ID: {}", request.productId());
        Product foundProduct = findById(request.productId());

        checkDoSendNotification(foundProduct);

        foundProduct.setQuantity(request.quantity());
        log.info("Saved new quantity of product.");
        return new ResponseMessageDTO("The product quantity was updated successfully.");
    }

    private void checkDoSendNotification(Product product) {
        log.debug("Checking if product quantity is equal to 0");
        if (product.getQuantity() != 0) return;
        eventPublisher.publishEvent(new ProductAvailableEvent(this, product.getId(), product.getName()));
    }

    public void deleteById(Long id) {
        log.info("Deleting product for ID: {}", id);
        productRepository.deleteById(id);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    protected Product findWithPromotionById(Long id) {
        return productRepository.findWithPromotionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @NotNull
    private LocalDateTime getLocalDataTime30DaysAgo() {
        return LocalDateTime.now(clock).minusDays(30);
    }
}
