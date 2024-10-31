package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProducerService producerService;
    private final SubcategoryService subcategoryService;
    private final ProductTypeService productTypeService;
    private final Clock clock = Clock.systemUTC();

    public ProductWithProducerAndPromotionDTO getProductById(Long id) {
        ProductWithProducerAndPromotionDTO product =  productRepository.findProductById(id, getLocalDataTime30DaysAgo());
        if(product.id() == null) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        return product;
    }

    public List<ProductResponse> getAllProducts() {
        return productMapper.mapToDto(productRepository.findAll());
    }

    @Transactional
    public ProductResponse saveProduct(Long subcategoryId, Long producerId, ProductRequest productRequest) {
        Product product = productMapper.mapToEntity(productRequest);
        product.setSubcategory(subcategoryService.findSubcategoryById(subcategoryId));
        product.setProducer(producerService.findProducerById(producerId));
        product.setType(productTypeService.findProductTypeById(productRequest.getProductTypeId()));

        productRepository.save(product);
        return productMapper.mapToDto(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long subcategoryId,
                                         Long producerId,
                                         Long productId,
                                         ProductRequest productRequest) {
        Product foundProduct = findProductById(productId);
        Product product = productMapper.mapToEntity(productRequest);

        foundProduct.setSubcategory(subcategoryService.findSubcategoryById(subcategoryId));
        foundProduct.setProducer(producerService.findProducerById(producerId));
        foundProduct.setType(productTypeService.findProductTypeById(productRequest.getProductTypeId()));
        foundProduct.setName(product.getName());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setQuantity(product.getQuantity());
        foundProduct.setImageUrl(product.getImageUrl());

        productRepository.save(foundProduct);
        return productMapper.mapToDto(foundProduct);
    }

    public void deleteProductById(Long id) {
        Product foundProduct = findProductById(id);
        productRepository.delete(foundProduct);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    protected Product findProductByIdWithPromotion(Long id) {
        return productRepository.findProductByIdWithPromotion(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @NotNull
    private LocalDateTime getLocalDataTime30DaysAgo() {
        return LocalDateTime.now(clock).minusDays(30);
    }
}
