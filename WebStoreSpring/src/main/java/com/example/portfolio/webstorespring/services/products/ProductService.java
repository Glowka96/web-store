package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final Clock clock = Clock.systemUTC();

    public ProductWithProducerAndPromotionDTO getById(Long id) {
        log.info("Fetching product for ID: {}", id);
        ProductWithProducerAndPromotionDTO product = productRepository.findById(id, getLocalDataTime30DaysAgo());
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        return product;
    }

    public List<ProductResponse> getAll() {
        log.info("Fetching all products.");
        return ProductMapper.mapToDto(productRepository.findAll());
    }

    @Transactional
    public ProductResponse save(Long subcategoryId, Long producerId, ProductRequest productRequest) {
        log.info("Saving product from request: {}", productRequest);
        Product product = ProductMapper.mapToEntity(productRequest);

        log.debug("Finding others entities and setting them.");
        product.setSubcategory(subcategoryService.findById(subcategoryId));
        product.setProducer(producerService.findById(producerId));
        product.setType(productTypeService.findById(productRequest.productTypeId()));

        productRepository.save(product);
        log.info("Saved product.");
        return ProductMapper.mapToDto(product);
    }

    @Transactional
    public ProductResponse update(Long subcategoryId,
                                  Long producerId,
                                  Long productId,
                                  ProductRequest productRequest) {
        log.info("Updating product for ID: {}, from request: {}", productId, productRequest);
        Product foundProduct = findById(productId);
        Product product = ProductMapper.mapToEntity(productRequest);

        log.debug("Finding others entities. Setting found product fields.");
        foundProduct.setSubcategory(subcategoryService.findById(subcategoryId));
        foundProduct.setProducer(producerService.findById(producerId));
        foundProduct.setType(productTypeService.findById(productRequest.productTypeId()));
        foundProduct.setName(product.getName());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setQuantity(product.getQuantity());
        foundProduct.setImageUrl(product.getImageUrl());

        productRepository.save(foundProduct);
        log.debug("Saved product");
        return ProductMapper.mapToDto(foundProduct);
    }

    public void deleteById(Long id) {
        log.info("Deleting product for ID: {}", id);
        productRepository.deleteById(id);
    }

    private Product findById(Long id) {
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
