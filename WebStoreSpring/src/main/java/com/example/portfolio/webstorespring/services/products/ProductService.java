package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.exceptions.SearchNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProducerRepository producerRepository;
    private final SubcategoryRepository subcategoryRepository;

    public ProductResponse getProductDtoById(Long id) {
        Product foundProduct = findProductById(id);
        return productMapper.mapToDto(foundProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productMapper.mapToDto(productRepository.findAll());
    }

    public List<ProductResponse> getAllProductsBySubcategoryId(Long subcategoryId,
                                                              Integer pageNo,
                                                              Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<Product> productPage = findPageProductsBySubcategoryId(subcategoryId, pageable);
        return productPage.map(productMapper::mapToDto).getContent();
    }

    public List<ProductResponse> getAllProductsBySubcategoryId(Long subcategoryId,
                                                              Integer pageNo,
                                                              Integer pageSize,
                                                              String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        Page<Product> productPage = findPageProductsBySubcategoryId(subcategoryId, pageable);
        return productPage.map(productMapper::mapToDto).getContent();
    }

    public Long getQuantityOfProductsBySubcategoryId(Long subcategoryId) {
        return productRepository.countProductBySubcategory_Id(subcategoryId);
    }

    public List<ProductResponse> getSearchProducts(String text, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        Page<Product> productPage = searchProductsByText(text, pageable);
        return productPage.map(productMapper::mapToDto).getContent();
    }

    public Long getAmountSearchProducts(String text) {
        return productRepository
                .countProductByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrProducerName(text, text, text);
    }

    public ProductResponse save(Long subcategoryId, Long producerId, ProductRequest productRequest) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);

        Product product = productMapper.mapToEntity(productRequest);
        product.setSubcategory(foundSubcategory);
        product.setProducer(foundProducer);

        productRepository.save(product);
        return productMapper.mapToDto(product);
    }

    public ProductResponse updateProduct(Long subcategoryId,
                                        Long producerId,
                                        Long productId,
                                        ProductRequest productRequest) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);
        Product foundProduct = findProductById(productId);
        Product product = productMapper.mapToEntity(productRequest);

        setupProduct(foundSubcategory, foundProducer, foundProduct, product);

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

    private Page<Product> findPageProductsBySubcategoryId(Long subCategoryId, Pageable pageable) {
        return productRepository.findProductBySubcategory_Id(subCategoryId, pageable)
                .orElseThrow(() -> new ResourceNotFoundException("Products", "page number", pageable.getPageNumber()));

    }

    private Page<Product> searchProductsByText(String text, Pageable pageable) {
        return productRepository
                .searchProductByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrProducerName(text,
                        text,
                        text,
                        pageable)
                .orElseThrow(SearchNotFoundException::new);
    }

    private Subcategory findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
    }

    private Producer findProducerById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }

    private void setupProduct(Subcategory foundSubcategory,
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