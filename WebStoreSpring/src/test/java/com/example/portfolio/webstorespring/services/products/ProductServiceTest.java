package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SubcategoryRepository subcategoryRepository;
    @Mock
    private ProducerRepository producerRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private ProductService underTest;

    private Product product;
    private ProductRequest productRequest;
    private Subcategory subcategory;
    private Producer producer;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.of(
            2023,
            3,
            9,
            12,
            30,
            30,
            0,
            ZoneId.of("GMT")
    );

    @BeforeEach
    void initialization() {
        ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
        ReflectionTestUtils.setField(productMapper, "productTypeMapper", productTypeMapper);

        subcategory = Subcategory.builder()
                .id(1L)
                .build();
        subcategory.setId(1L);

        producer = Producer.builder()
                .id(1L)
                .build();

        ProductType productType = ProductType.builder()
                .name("Test")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test")
                .price(BigDecimal.valueOf(29.99))
                .description("This is description")
                .subcategory(subcategory)
                .producer(producer)
                .type(productType)
                .build();

        ProductTypeRequest productTypeRequest = ProductTypeRequest
                .builder()
                .name("test")
                .build();

        productRequest = ProductRequest.builder()
                .name("Test")
                .price(BigDecimal.valueOf(29.99))
                .description("This is description")
                .type(productTypeRequest)
                .build();
    }

    @Test
    void shouldGetProductById() {
        // given
        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
        when(clock.instant()).thenReturn(zonedDateTime.toInstant());

        ProductWithProducerAndPromotionDTO productDTO = new ProductWithProducerAndPromotionDTO(1L,
                "Test",
                "test.pl/test.png",
                1L, "Test",
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(90L),
                BigDecimal.valueOf(70L),
                Date.from(LocalDateTime.now(clock).plusDays(15).atZone(ZoneId.systemDefault()).toInstant()),
                "Test description",
                "Test producer");

        given(productRepository.findProductById(anyLong(), any())).willReturn(productDTO);

        // when
        ProductWithProducerAndPromotionDTO result = underTest.getProductById(1L);

        // then
        assertThat(result).isNotNull().isEqualTo(productDTO);
    }

    @Test
    void willThrowWhenProductIdNotFound() {
        // given
        given(productRepository.findProductById(anyLong(), any())).willReturn(new ProductWithProducerAndPromotionDTO(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));

        // when
        // then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getProductById(1L));
        verify(productRepository, times(1)).findProductById(eq(1L), any());
    }

    @Test
    void shouldGetAllProducts() {
        // when
        underTest.getAllProducts();
        // then
        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldSaveProduct() {
        // given
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));

        // when
        ProductResponse savedProductResponse = underTest.saveProduct(subcategory.getId(), producer.getId(), productRequest);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();
        ProductResponse mappedProductRequest = productMapper.mapToDto(capturedProduct);

        assertThat(mappedProductRequest).isEqualTo(savedProductResponse);
    }

    @Test
    void willThrowWhenSubCategoryIdNotFound() {
        // given
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.saveProduct(2L, 1L, productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Subcategory with id 2 not found");
    }

    @Test
    void willThrowWhenProducerIdNotFound() {
        // given
        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.saveProduct(1L, 2L, productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldUpdateProduct() {
        // given
        productRequest.setName("New name");
        productRequest.setDescription("New description");
        productRequest.setPrice(BigDecimal.valueOf(10.00));

        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        ProductResponse updatedProductResponse = underTest.updateProduct(subcategory.getId(), producer.getId(), product.getId(), productRequest);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();
        ProductResponse mappedProductRequest = productMapper.mapToDto(capturedProduct);

        assertThat(mappedProductRequest).isEqualTo(updatedProductResponse);
    }

    @Test
    void shouldDeleteById() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        underTest.deleteProductById(1L);

        // then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }
}
