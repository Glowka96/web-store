package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public List<ProductTypeResponse> getAll() {
        return ProductTypeMapper.mapToDto(productTypeRepository.findAll());
    }

    public ProductTypeResponse save(ProductTypeRequest productTypeRequest){
        ProductType productType = ProductTypeMapper.mapToEntity(productTypeRequest);
        productTypeRepository.save(productType);
        return ProductTypeMapper.mapToDto(productType);
    }

    @Transactional
    public ProductTypeResponse update(Long id, ProductTypeRequest productTypeRequest) {
        ProductType foundProductType = findById(id);
        foundProductType.setName(productTypeRequest.name());
        productTypeRepository.save(foundProductType);
        return ProductTypeMapper.mapToDto(foundProductType);
    }

    public void deleteById(Long productTypeId) {
        productTypeRepository.deleteById(productTypeId);
    }

    protected ProductType findById(Long productTypeId) {
        return productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductType", "id", productTypeId));
    }
}
