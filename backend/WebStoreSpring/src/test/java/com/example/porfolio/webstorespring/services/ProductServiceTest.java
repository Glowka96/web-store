package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import com.example.porfolio.webstorespring.repositories.ProducerRepository;
import com.example.porfolio.webstorespring.repositories.ProductRepository;
import com.example.porfolio.webstorespring.repositories.SubcategoryRepository;
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
    private ProductDto productDto;
    private ProductDto productDto2;
    private ProductDto productDto3;
    private Subcategory subCategory;
    private SubcategoryDto subcategoryDto;
    private Producer producer;
    private ProducerDto producerDto;

    @BeforeEach
    void initialization() {
        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        subCategory = new Subcategory();
        subCategory.setId(1L);

        subcategoryDto = new SubcategoryDto();
        subcategoryDto.setId(1L);

        producer = new Producer();
        producer.setId(1L);

        producerDto = new ProducerDto();
        producerDto.setId(1L);

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

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test");
        productDto.setPrice(29.99);
        productDto.setDescription("This is description");
        productDto.setSubcategoryDto(subcategoryDto);
        productDto.setProducerDto(producerDto);

        productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("Test");
        productDto2.setPrice(25.99);
        productDto2.setDescription("This is description");
        productDto2.setSubcategoryDto(subcategoryDto);
        productDto2.setProducerDto(producerDto);

        productDto3 = new ProductDto();
        productDto3.setId(3L);
        productDto3.setName("Test");
        productDto3.setPrice(22.99);
        productDto3.setDescription("This is description");
        productDto3.setSubcategoryDto(subcategoryDto);
        productDto3.setProducerDto(producerDto);
    }


    @Test
    void shouldGetProductDtoById() {
        // given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        productDto = underTest.getProductDtoById(1L);

        // then
        assertThat(productDto).isNotNull();
        assertThat(productDto.getName()).isEqualTo(product.getName());
        assertThat(productDto.getDescription()).isEqualTo(product.getDescription());
        assertThat(productDto.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    void shouldGetAllProductsBySubCategoryId_WhenGetSubCategoryId_PageNo_PageSize() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<Product> productList = Arrays.asList(product, product2, product3);

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        List<ProductDto> exceptedList = Arrays.asList(productDto, productDto2, productDto3);

        given(productRepository.findProductBySubcategory_Id(eq(subCategory.getId()), any(Pageable.class)))
                .willReturn(Optional.of(productPage));

        // when
        List<ProductDto> actualProductDtoList = underTest.getAllProductsBySubCategoryId(subCategory.getId(), 0, 5);

        // then
        assertThat(actualProductDtoList).isNotNull();
        assertThat(actualProductDtoList).isEqualTo(exceptedList);
        verify(productRepository, times(1)).findProductBySubcategory_Id(subCategory.getId(), pageable);
    }

    @Test
    void shouldGetAllProductsBySubCategoryId_WhenGetSubCategoryId_PageNo_PageSize_SortBy() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "price"));
        List<Product> productList = Arrays.asList(product3, product2, product);

        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        List<ProductDto> exceptedProductDtoList = Arrays.asList(productDto3, productDto2, productDto);

        given(productRepository.findProductBySubcategory_Id(subCategory.getId(), pageable))
                .willReturn(Optional.of(productPage));

        // when
        List<ProductDto> actualProductDtoList = underTest.getAllProductsBySubCategoryId(subCategory.getId(), 0, 5, "price");

        // then
        assertThat(actualProductDtoList).isNotNull();
        assertThat(actualProductDtoList).isEqualTo(exceptedProductDtoList);
        verify(productRepository, times(1)).findProductBySubcategory_Id(subCategory.getId(), pageable);
    }

    @Test
    void shouldSaveProduct() {
        // given
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));

        // when
        underTest.save(subCategory.getId(), producer.getId(), productDto);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();
        ProductDto mappedProductDto = productMapper.mapToDto(capturedProduct);

        assertThat(mappedProductDto).isEqualTo(productDto);
    }

    @Test
    void willThrowWhenSubCategoryIdNotFound() {
        // given
        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.save(2L, 1L, productDto))
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
        assertThatThrownBy(() -> underTest.save(1L, 2L, productDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldUpdateProduct() {
        // given
        productDto.setName("New name");
        productDto.setDescription("New description");
        productDto.setPrice(10.00);

        given(subCategoryRepository.findById(anyLong())).willReturn(Optional.of(subCategory));
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        underTest.updateProduct(subCategory.getId(), producer.getId(), product.getId(), productDto);

        // then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();
        ProductDto mappedProductDto = productMapper.mapToDto(capturedProduct);

        assertThat(mappedProductDto).isEqualTo(productDto);
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