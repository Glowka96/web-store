package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
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
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper.createProduct;
import static com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper.createProductRequest;
import static com.example.portfolio.webstorespring.buildhelpers.ProductWithProducerAndPromotionDTOBuilderHelper.createNullProductWithProducerAndPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.ProductWithProducerAndPromotionDTOBuilderHelper.createProductWithProducerAndPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.SubcategoryBuilderHelper.createSubcategory;
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
    @InjectMocks
    private ProductService underTest;

    @BeforeEach
    void initialization() {
        ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
        ReflectionTestUtils.setField(productMapper, "productTypeMapper", productTypeMapper);
    }

    @Test
    void shouldGetProductById() {
        // given
        ProductWithProducerAndPromotionDTO productWithProducerAndPromotionDTO = createProductWithProducerAndPromotionDTO();
        given(productRepository.findProductById(anyLong(), any())).willReturn(productWithProducerAndPromotionDTO);

        // when
        ProductWithProducerAndPromotionDTO result = underTest.getProductById(1L);

        // then
        assertThat(result).isNotNull().isEqualTo(productWithProducerAndPromotionDTO);
    }

    @Test
    void willThrowWhenProductIdNotFound() {
        // given
        ProductWithProducerAndPromotionDTO productWithProducerAndPromotionDTO = createNullProductWithProducerAndPromotionDTO();

        given(productRepository.findProductById(anyLong(), any())).willReturn(productWithProducerAndPromotionDTO);
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
        Producer producer = createProducer();
        Subcategory subcategory = createSubcategory();
        ProductRequest productRequest = createProductRequest();

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
        ProductRequest productRequest = createProductRequest();

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
        Subcategory subcategory = createSubcategory();
        ProductRequest productRequest = createProductRequest();

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
        ProductRequest productRequest = ProductBuilderHelper.createProductRequest(
                "Test 2",
                "Test description 2",
                BigDecimal.valueOf(11.0),
                8L
        );

        Subcategory subcategory = createSubcategory();
        Producer producer = createProducer();
        Product product = createProduct();

        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        Long quantity = product.getQuantity();

        given(subcategoryRepository.findById(anyLong())).willReturn(Optional.of(subcategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        ProductResponse updatedProductResponse = underTest.updateProduct(subcategory.getId(), producer.getId(), product.getId(), productRequest);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        ProductResponse mappedProductRequest =
                productMapper.mapToDto(productArgumentCaptor.getValue());

        assertThat(mappedProductRequest).isEqualTo(updatedProductResponse);
        assertThat(updatedProductResponse.getName()).isNotEqualTo(name);
        assertThat(updatedProductResponse.getDescription()).isNotEqualTo(description);
        assertThat(updatedProductResponse.getPrice()).isNotEqualTo(price);
        assertThat(updatedProductResponse.getQuantity()).isNotEqualTo(quantity);
    }

    @Test
    void shouldDeleteById() {
        // given
        Product product = createProduct();
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        underTest.deleteProductById(1L);

        // then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }
}
