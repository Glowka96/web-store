package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.ProducerRepository;
import com.example.porfolio.webstorespring.repositories.ProductRepository;
import com.example.porfolio.webstorespring.repositories.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProducerRepository producerRepository;
    private final SubcategoryRepository subcategoryRepository;

    public ProductDto getProductDtoById(Long id) {
        Product foundProduct = findProductById(id);
        return productMapper.mapToDto(foundProduct);
    }

    public List<ProductDto> getAllProductsBySubCategoryId(Long subcategoryId,
                                                          Integer pageNo,
                                                          Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<Product> productPage = findPageProductsBySubcategoryId(subcategoryId, pageable);
        return productPage.map(productMapper::mapToDto).getContent();
    }

    public List<ProductDto> getAllProductsBySubCategoryId(Long subcategoryId,
                                                          Integer pageNo,
                                                          Integer pageSize,
                                                          String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        Page<Product> productPage = findPageProductsBySubcategoryId(subcategoryId, pageable);
        return productPage.map(productMapper::mapToDto).getContent();
    }

    public ProductDto save(Long subcategoryId, Long producerId, ProductDto productDto) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);

        Product product = productMapper.mapToEntity(productDto);
        product.setSubcategory(foundSubcategory);
        product.setProducer(foundProducer);

        productRepository.save(product);
        return productMapper.mapToDto(product);
    }

    public ProductDto updateProduct(Long subcategoryId,
                                    Long producerId,
                                    Long productId,
                                    ProductDto productDto) {
        Subcategory foundSubcategory = findSubcategoryById(subcategoryId);
        Producer foundProducer = findProducerById(producerId);
        Product foundProduct = findProductById(productId);
        Product product = productMapper.mapToEntity(productDto);

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
