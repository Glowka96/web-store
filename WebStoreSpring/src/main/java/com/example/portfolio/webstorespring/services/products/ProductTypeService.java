package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public List<ProductTypeResponse> getAll() {
        log.info("Fetching all product type.");
        return ProductTypeMapper.mapToDto(productTypeRepository.findAll());
    }

    public ProductTypeResponse save(ProductTypeRequest productTypeRequest){
        log.info("Saving product type from request: {}", productTypeRequest);
        ProductType productType = ProductTypeMapper.mapToEntity(productTypeRequest);
        productTypeRepository.save(productType);
        log.info("Saved product type.");
        return ProductTypeMapper.mapToDto(productType);
    }

    @Transactional
    public ProductTypeResponse update(Long id, ProductTypeRequest productTypeRequest) {
        log.info("Updating product type for ID: {}, from: {}", id, productTypeRequest);
        ProductType foundProductType = findById(id);
        foundProductType.setName(productTypeRequest.name());
        productTypeRepository.save(foundProductType);
        log.info("Updated product type.");
        return ProductTypeMapper.mapToDto(foundProductType);
    }

    public void deleteById(Long id) {
        log.info("Deleting product type for ID: {}", id);
        productTypeRepository.deleteById(id);
    }

    protected ProductType findById(Long id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductType", "id", id));
    }
}
