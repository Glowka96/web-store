package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.models.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;
    @InjectMocks
    private ProductTypeService underTest;

    @Test
    void shouldGetAllProductTypes() {
        List<ProductType> productTypes = Collections.singletonList(createProductType());
        List<ProductTypeResponse> expectedResponses = Collections.singletonList(createProductTypeResponse());

        given(productTypeRepository.findAll()).willReturn(productTypes);

        List<ProductTypeResponse> foundProductTypeResponses = underTest.getAll();

        assertEquals(expectedResponses, foundProductTypeResponses);
        verify(productTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(productTypeRepository);
    }

    @Test
    void shouldSaveProductType() {
        ProductTypeRequest productTypeRequest = createProductTypeRequest();

        ProductTypeResponse result = underTest.save(productTypeRequest);

        ArgumentCaptor<ProductType> productTypeArgumentCaptor =
                ArgumentCaptor.forClass(ProductType.class);
        verify(productTypeRepository).save(productTypeArgumentCaptor.capture());

        ProductTypeResponse mappedProductTypeResponse = ProductTypeMapper.mapToDto(productTypeArgumentCaptor.getValue());

        assertEquals(mappedProductTypeResponse, result);
    }

    @Test
    void shouldUpdateProductType() {
        ProductType productType = createProductType();
        String productTypeNameBeforeUpdate = productType.getName();
        ProductTypeRequest productTypeRequest = createProductTypeRequest("Test2");

        given(productTypeRepository.findById(anyLong())).willReturn(Optional.of(productType));

        ProductTypeResponse updatedProductTypeResponse = underTest.update(1L, productTypeRequest);

        ArgumentCaptor<ProductType> productTypeArgumentCaptor =
                ArgumentCaptor.forClass(ProductType.class);
        verify(productTypeRepository).save(productTypeArgumentCaptor.capture());

        ProductTypeResponse mappedProductType =
                ProductTypeMapper.mapToDto(productTypeArgumentCaptor.getValue());

        assertEquals(mappedProductType, updatedProductTypeResponse);
        assertNotEquals(productTypeNameBeforeUpdate, updatedProductTypeResponse.name());
    }

    @Test
    void deleteProductTypeById() {
        underTest.deleteById(anyLong());

        verify(productTypeRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(productTypeRepository);
    }
}
