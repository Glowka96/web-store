package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProductRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithProducerAndPromotionDTOBuilderHelper.createNullProductWithProducerAndPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithProducerAndPromotionDTOBuilderHelper.createProductWithProducerAndPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Assertions.assertThat;
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
    private SubcategoryService subcategoryService;
    @Mock
    private ProducerService producerService;
    @Mock
    private ProductTypeService productTypeService;
    @InjectMocks
    private ProductService underTest;

    @BeforeEach
    void initialization() {
        ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
        ReflectionTestUtils.setField(productMapper, "productTypeMapper", productTypeMapper);
        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);
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
    void willThrowWhenProductIdWithPromotionNotFound() {
        given(productRepository.findProductByIdWithPromotion(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThrows(ResourceNotFoundException.class, () -> underTest.findProductByIdWithPromotion(1L));
        verify(productRepository, times(1)).findProductByIdWithPromotion(eq(1L));
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
        ProductType productType = createProductType();

        given(producerService.findProducerById(anyLong())).willReturn(producer);
        given(subcategoryService.findSubcategoryById(anyLong())).willReturn(subcategory);
        given(productTypeService.findProductTypeById(anyLong())).willReturn(productType);

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
    void shouldUpdateProduct() {
        // given
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

        given(subcategoryService.findSubcategoryById(anyLong())).willReturn(subcategory);
        given(producerService.findProducerById(anyLong())).willReturn(producer);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productTypeService.findProductTypeById(anyLong())).willReturn(productType);

        // when
        ProductResponse updatedProductResponse = underTest.updateProduct(subcategory.getId(), producer.getId(), product.getId(), productRequest);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        ProductResponse mappedProductRequest =
                productMapper.mapToDto(productArgumentCaptor.getValue());

        assertThat(mappedProductRequest).isEqualTo(updatedProductResponse);
        assertThat(updatedProductResponse.getName()).isNotEqualTo(beforeUpdateName);
        assertThat(updatedProductResponse.getDescription()).isNotEqualTo(beforeUpdateDescription);
        assertThat(updatedProductResponse.getPrice()).isNotEqualTo(beforeUpdatePrice);
        assertThat(updatedProductResponse.getQuantity()).isNotEqualTo(beforeUpdateQuantity);
    }

    @Test
    void shouldDeleteById() {
        // given
        Product product = make(a(BASIC_PRODUCT));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        underTest.deleteProductById(1L);

        // then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }
}
