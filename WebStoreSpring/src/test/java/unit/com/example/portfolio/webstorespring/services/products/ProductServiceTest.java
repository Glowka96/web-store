package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProductRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithProducerAndPromotionDTOBuilderHelper.createProductWithProducerAndPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private SubcategoryService subcategoryService;
    @Mock
    private ProducerService producerService;
    @Mock
    private ProductTypeService productTypeService;
    @InjectMocks
    private ProductService underTest;

    @Test
    void shouldGetProductById() {
        ProductWithProducerAndPromotionDTO productWithProducerAndPromotionDTO = createProductWithProducerAndPromotionDTO();
        given(productRepository.findById(anyLong(), any())).willReturn(productWithProducerAndPromotionDTO);

        ProductWithProducerAndPromotionDTO result = underTest.getById(1L);

        assertThat(result).isNotNull().isEqualTo(productWithProducerAndPromotionDTO);
    }

    @Test
    void willThrowResourceNotFoundException_whenProductNotFound() {
        given(productRepository.findById(anyLong(), any())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> underTest.getById(1L));
        verify(productRepository, times(1)).findById(eq(1L), any());
    }

    @Test
    void willThrowResourceNotFoundException_whenProductIdWithPromotionNotFound() {
        given(productRepository.findWithPromotionById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> underTest.findWithPromotionById(1L));
        verify(productRepository, times(1)).findWithPromotionById(1L);
    }

    @Test
    void shouldGetAllProducts() {
        underTest.getAll();

        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldSaveProduct() {
        Producer producer = createProducer();
        Subcategory subcategory = createSubcategory();
        ProductRequest productRequest = createProductRequest();
        ProductType productType = createProductType();

        given(producerService.findById(anyLong())).willReturn(producer);
        given(subcategoryService.findById(anyLong())).willReturn(subcategory);
        given(productTypeService.findById(anyLong())).willReturn(productType);

        ProductResponse savedProductResponse = underTest.save(subcategory.getId(), producer.getId(), productRequest);

        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        ProductResponse mappedProductRequest = ProductMapper.mapToDto(productArgumentCaptor.getValue());

        assertEquals(mappedProductRequest, savedProductResponse);
    }

    @Test
    void shouldUpdateProduct() {
        ProductRequest productRequest = ProductBuilderHelper.createProductRequest(
                "Test 2",
                "Test description 2",
                BigDecimal.valueOf(11.0),
                8L
        );

        Product product = make(a(BASIC_PRODUCT));

        Subcategory subcategory = product.getSubcategory();
        Producer producer = product.getProducer();
        ProductType productType = product.getType();

        String beforeUpdateName = product.getName();
        String beforeUpdateDescription = product.getDescription();
        BigDecimal beforeUpdatePrice = product.getPrice();
        Long beforeUpdateQuantity = product.getQuantity();

        given(subcategoryService.findById(anyLong())).willReturn(subcategory);
        given(producerService.findById(anyLong())).willReturn(producer);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productTypeService.findById(anyLong())).willReturn(productType);

        ProductResponse updatedProductResponse = underTest.update(subcategory.getId(), producer.getId(), product.getId(), productRequest);

        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        ProductResponse mappedProductRequest =
                ProductMapper.mapToDto(productArgumentCaptor.getValue());

        assertEquals(mappedProductRequest, updatedProductResponse);
        assertNotEquals(beforeUpdateName, updatedProductResponse.name());
        assertNotEquals(beforeUpdateDescription, updatedProductResponse.description());
        assertNotEquals(beforeUpdatePrice, updatedProductResponse.price());
        assertNotEquals(beforeUpdateQuantity, updatedProductResponse.quantity());
    }

    @Test
    void shouldDeleteById() {
        underTest.deleteById(anyLong());

        verify(productRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(productRepository);
    }
}
