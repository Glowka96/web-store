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
    private final ProductTypeMapper productTypeMapper;

    public List<ProductTypeResponse> getAll() {
        return productTypeMapper.mapToDto(productTypeRepository.findAll());
    }

    public ProductTypeResponse save(ProductTypeRequest productTypeRequest){
        ProductType productType = productTypeMapper.mapToEntity(productTypeRequest);
        productTypeRepository.save(productType);
        return productTypeMapper.mapToDto(productType);
    }

    @Transactional
    public ProductTypeResponse update(Long id, ProductTypeRequest productTypeRequest) {
        ProductType foundProductType = findById(id);
        ProductType productType = productTypeMapper.mapToEntity(productTypeRequest);

        foundProductType.setName(productType.getName());
        productTypeRepository.save(foundProductType);

        return productTypeMapper.mapToDto(foundProductType);
    }

    public void deleteById(Long productTypeId) {
        productTypeRepository.deleteById(productTypeId);
    }

    protected ProductType findById(Long productTypeId) {
        return productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductType", "id", productTypeId));
    }
}
