package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.repositories.products.ProductTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;
    @Spy
    private ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
    @InjectMocks
    private ProductTypeService underTest;

    @Test
    void shouldGetAllProductTypes() {
        // given
        List<ProductType> productTypes = Collections.singletonList(new ProductType());
        List<ProductTypeResponse> expectedResponses = Collections.singletonList(new ProductTypeResponse());

        given(productTypeRepository.findAll()).willReturn(productTypes);

        // when
        List<ProductTypeResponse> foundProductTypeResponses = underTest.getAllProductType();

        // then
        assertThat(foundProductTypeResponses).isEqualTo(expectedResponses);
        verify(productTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(productTypeRepository);
        verify(productTypeMapper).mapToDto(productTypes);
    }

    @Test
    void shouldSaveProductType(){
        // given
        ProductTypeRequest productTypeRequest = createProductTypeRequest();

        // when
        ProductTypeResponse result = underTest.saveProductType(productTypeRequest);

        // then
        ArgumentCaptor<ProductType> productTypeArgumentCaptor =
                ArgumentCaptor.forClass(ProductType.class);
        verify(productTypeRepository).save(productTypeArgumentCaptor.capture());

        ProductType capturedProductType = productTypeArgumentCaptor.getValue();
        ProductTypeResponse mappedProductTypeResponse = productTypeMapper.mapToDto(capturedProductType);

        assertThat(result).isEqualTo(mappedProductTypeResponse);
    }

    @Test
    void shouldUpdateProductType() {
        // given
        ProductType productType = createProductType();
        String productTypeName = productType.getName();
        ProductTypeRequest productTypeRequest = createProductTypeRequest("Test2");

        given(productTypeRepository.findById(anyLong())).willReturn(Optional.of(productType));

        // when
        ProductTypeResponse updatedProductTypeResponse = underTest.updateProductType(1L, productTypeRequest);

        // then
        ArgumentCaptor<ProductType> productTypeArgumentCaptor =
                ArgumentCaptor.forClass(ProductType.class);
        verify(productTypeRepository).save(productTypeArgumentCaptor.capture());

        ProductTypeResponse mappedProductType =
                productTypeMapper.mapToDto(productTypeArgumentCaptor.getValue());

        assertThat(mappedProductType).isEqualTo(updatedProductTypeResponse);
        assertThat(updatedProductTypeResponse.getName()).isNotEqualTo(productTypeName);
    }

    @Test
    void deleteProductTypeById() {
        // given
        ProductType foundProductType = createProductType();

        given(productTypeRepository.findById(anyLong())).willReturn(Optional.of(foundProductType));

        // when
        underTest.deleteProductTypeById(1L);

        // then
        verify(productTypeRepository, times(1)).findById(1L);
        verify(productTypeRepository, times(1)).delete(foundProductType);
    }
}
