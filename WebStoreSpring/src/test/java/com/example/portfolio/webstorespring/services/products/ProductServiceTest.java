package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
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
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SubcategoryRepository subCategoryRepository;
    @Mock
    private ProducerRepository producerRepository;
    @InjectMocks
    private ProductService underTest;

    private Product product;
    private Product product2;
    private Product product3;
    private ProductRequest productRequest;
    private ProductRequest productRequest2;
    private ProductRequest productRequest3;
    private Subcategory subCategory;
    private Producer producer;

    @BeforeEach
    void initialization() {
        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        subCategory = new Subcategory();
        subCategory.setId(1L);

        producer = new Producer();
        producer.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setPrice(29.99);
        product.setDescription("This is description");
        product.setSubcategory(subCategory);
        product.setProducer(producer);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Test");
        product2.setPrice(25.99);
        product2.setDescription("This is description");
        product2.setSubcategory(subCategory);
        product2.setProducer(producer);

        product3 = new Product();
        product3.setId(3L);
        product3.setName("Test");
        product3.setPrice(22.99);
        product3.setDescription("This is description");
        product3.setSubcategory(subCategory);
        product3.setProducer(producer);

        productRequest = new ProductRequest();
        productRequest.setName("Test");
        productRequest.setPrice(29.99);
        productRequest.setDescription("This is description");

        productRequest2 = new ProductRequest();
        productRequest2.setName("Test");
        productRequest2.setPrice(25.99);
        productRequest2.setDescription("This is description");

        productRequest3 = new ProductRequest();
        productRequest3.setName("Test");
        productRequest3.setPrice(22.99);
        productRequest3.setDescription("This is description");
    }


    @Test
    void shouldGetProductDtoById() {
        // given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        ProductResponse foundProductResponse = underTest.getProductDtoById(1L);

        // then
        assertThat(foundProductResponse).isNotNull();
        assertThat(foundProductResponse.getName()).isEqualTo(product.getName());
        assertThat(foundProductResponse.getDescription()).isEqualTo(product.getDescription());
        assertThat(foundProductResponse.getPrice()).isEqualTo(product.getPrice());
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
    void shouldGetAllProductsBySubCategoryId_WhenGetSubCategoryId_PageNo_PageSize() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<Product> productList = Arrays.asList(product, product2, product3);

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(Pageable.class)))
                .willReturn(Optional.of(productPage));

        // when
        List<ProductResponse> foundProductResponses = underTest.getAllProductsBySubcategoryId(subCategory.getId(), 0, 5);

        // then
        assertThat(foundProductResponses).hasSize(3);
        verify(productRepository, times(1)).findProductsBySubcategory_Id(subCategory.getId(), pageable);
    }

    @Test
    void shouldGetAllProductsBySubCategoryId_WhenGetSubCategoryId_PageNo_PageSize_SortBy() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "price"));
        List<Product> productList = Arrays.asList(product3, product2, product);

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(Pageable.class)))
                .willReturn(Optional.of(productPage));

        // when
        List<ProductResponse> foundProductResponses = underTest.getAllProductsBySubcategoryId(subCategory.getId(), 0, 5, "price");

        // then
        assertThat(foundProductResponses).hasSize(3);
        verify(productRepository, times(1)).findProductsBySubcategory_Id(subCategory.getId(), pageable);
    }

    @Test
    void shouldGetAmountProductsBySubcategoryId() {
        // given
        Long amount = 12L;
        given(productRepository.countProductsBySubcategory_Id(anyLong())).willReturn(amount);

        // when
        Long actualProductDtoList = underTest.getQuantityOfProductsBySubcategoryId(1L);

        // then
        assertThat(actualProductDtoList).isEqualTo(amount);
    }

    @Test
    void shouldGetSearchProductsByText() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<Product> productList = Arrays.asList(product, product2, product3);

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.searchProductsByEnteredText(anyString(), any(Pageable.class)))
                .willReturn(Optional.of(productPage));

        // when
        List<ProductResponse> foundProductResponses = underTest.getSearchProducts("test", 0, 5, "id");

        // then
        assertThat(foundProductResponses).hasSize(3);
        verify(productRepository, times(1)).searchProductsByEnteredText("test", pageable);
    }

    @Test
    void shouldGetQuantitySearchProduct() {
        // given
        Long amount = 12L;
        given(productRepository.countProductsByEnteredText(anyString())).willReturn(amount);

        // when
        Long actual = underTest.getQuantitySearchProducts("test");

        // then
        assertThat(actual).isEqualTo(amount);
    }


    @Test
    void shouldSaveProduct() {
        // given
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));

        // when
        ProductResponse savedProductResponse = underTest.save(subCategory.getId(), producer.getId(), productRequest);

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
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.save(2L, 1L, productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("SubCategory with id 2 not found");
    }

    @Test
    void willThrowWhenProducerIdNotFound() {
        // given
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.save(1L, 2L, productRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldUpdateProduct() {
        // given
        productRequest.setName("New name");
        productRequest.setDescription("New description");
        productRequest.setPrice(10.00);

        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        ProductResponse updatedProductResponse = underTest.updateProduct(subCategory.getId(), producer.getId(), product.getId(), productRequest);

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
