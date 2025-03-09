package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductTypeResponse;
import com.example.portfolio.webstorespring.models.entities.products.ProductType;
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
        return ProductTypeMapper.mapToResponse(productTypeRepository.findAll());
    }

    public ProductTypeResponse save(ProductTypeRequest request){
        log.info("Saving product type from request: {}", request);
        ProductType productType = ProductTypeMapper.mapToEntity(request);
        productTypeRepository.save(productType);
        log.info("Saved product type.");
        return ProductTypeMapper.mapToResponse(productType);
    }

    @Transactional
    public ProductTypeResponse update(Long id, ProductTypeRequest request) {
        log.info("Updating product type for ID: {}, from: {}", id, request);
        ProductType foundProductType = findById(id);
        foundProductType.setName(request.name());
        productTypeRepository.save(foundProductType);
        log.info("Updated product type.");
        return ProductTypeMapper.mapToResponse(foundProductType);
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
