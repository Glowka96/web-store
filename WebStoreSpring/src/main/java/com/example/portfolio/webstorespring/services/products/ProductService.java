package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProducerRepository producerRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final Clock clock = Clock.systemUTC();

    public ProductWithProducerAndPromotionDTO getProductById(Long id) {
        ProductWithProducerAndPromotionDTO product =  productRepository.findProductById(id, getDate30DaysAgo());
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
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);

        Product product = productMapper.mapToEntity(productRequest);
        product.setSubcategory(foundSubcategory);
        product.setProducer(foundProducer);

        productRepository.save(product);
        return productMapper.mapToDto(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long subcategoryId,
                                         Long producerId,
                                         Long productId,
                                         ProductRequest productRequest) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);
        Product foundProduct = findProductById(productId);
        Product product = productMapper.mapToEntity(productRequest);

        setupUpdateProduct(foundSubcategory, foundProducer, foundProduct, product);

        productRepository.save(product);
        return productMapper.mapToDto(product);
    }

    public void deleteById(Long id) {
        Product foundProduct = findProductById(id);
        productRepository.delete(foundProduct);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @NotNull
    private Date getDate30DaysAgo() {
        return Date.from(LocalDateTime.now(clock).minusDays(30)
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private Subcategory findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
    }

    private Producer findProducerById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }

    private void setupUpdateProduct(Subcategory foundSubcategory,
                                    Producer foundProducer,
                                    Product foundProduct,
                                    Product updatedProduct) {
        updatedProduct.setSubcategory(foundSubcategory);
        updatedProduct.setProducer(foundProducer);
        updatedProduct.setId(foundProduct.getId());

        if (updatedProduct.getName() == null) {
            updatedProduct.setName(foundProduct.getName());
        }
        if (updatedProduct.getDescription() == null) {
            updatedProduct.setDescription(foundProduct.getDescription());
        }
        if (updatedProduct.getImageUrl() == null) {
            updatedProduct.setImageUrl(foundProduct.getImageUrl());
        }
        if (updatedProduct.getPrice() == null) {
            updatedProduct.setPrice(foundProduct.getPrice());
        }
        if (updatedProduct.getType() == null) {
            updatedProduct.setType(foundProduct.getType());
        }
    }
}
