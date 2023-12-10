package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.ProductType;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private Product product2;
    private Product product3;
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
        subcategory = new Subcategory();
        subcategory.setId(1L);

        producer = new Producer();
        producer.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setPrice(BigDecimal.valueOf(29.99));
        product.setDescription("This is description");
        product.setSubcategory(subcategory);
        product.setProducer(producer);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Test");
        product2.setPrice(BigDecimal.valueOf(25.99));
        product2.setDescription("This is description");
        product2.setSubcategory(subcategory);
        product2.setProducer(producer);

        product3 = new Product();
        product3.setId(3L);
        product3.setName("Test");
        product3.setPrice(BigDecimal.valueOf(22.99));
        product3.setDescription("This is description");
        product3.setSubcategory(subcategory);
        product3.setProducer(producer);

        productRequest = new ProductRequest();
        productRequest.setName("Test");
        productRequest.setPrice(BigDecimal.valueOf(29.99));
        productRequest.setDescription("This is description");
    }

    @Test
    void shouldGetProductById() {
        // given
        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
        when(clock.instant()).thenReturn(zonedDateTime.toInstant());

        ProductWithProducerAndPromotionDTO productDTO = new ProductWithProducerAndPromotionDTO(1L,
                "Test",
                "test.pl/test.png",
                1L, ProductType.TEST,
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
    void shouldGetPageProductsBySubCategoryId() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageProductsBySubCategoryIdReturnTwoTotalPages() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 2, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(2);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageNewProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetEmptyPageWhenNoHaveNewProduct() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> emptyList = new ArrayList<>();
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(emptyList, pageable, emptyList.size());

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(emptyList.size());
        assertThat(actual.totalPages()).isEqualTo(0);
        assertThat(actual.totalElements()).isEqualTo(0);
    }

    @Test
    void shouldPageProductsBySubCategoryIdWhenGetSubCategoryId_PageNo_PageSize_SortBy_SortDirection() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageProductsBySearchText() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.searchProductsByEnteredText(anyString(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageSearchProducts("test", 0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPromotionProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findPromotionProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPagePromotionProduct(0, 5, "id", "asc");

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @NotNull
    private ProductWithPromotionAndLowestPriceDTO getProductDTO() {
        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
        when(clock.instant()).thenReturn(zonedDateTime.toInstant());
        return new ProductWithPromotionAndLowestPriceDTO(
                1L,
                "Test",
                "test.pl/test.png",
                1L, ProductType.TEST,
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(90L),
                BigDecimal.valueOf(70L),
                Date.from(LocalDateTime.now(clock).plusDays(15).atZone(ZoneId.systemDefault()).toInstant())
        );
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
        underTest.deleteById(1L);

        // then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }
}
