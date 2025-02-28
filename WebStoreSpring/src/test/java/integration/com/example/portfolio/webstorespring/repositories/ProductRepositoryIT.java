package com.example.portfolio.webstorespring.repositories;

import com.example.portfolio.webstorespring.configs.ContainersConfig;
import com.example.portfolio.webstorespring.configs.InitProductConfig;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Import({ContainersConfig.class, InitProductConfig.class})
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT{

    @Autowired
    private InitProductTestData initProductTestData;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        initProductTestData.initTestData();
    }

    @Test
    void shouldFindProductWithoutPromotionById() {
        Optional<Product> optionalProduct = productRepository.findById(initProductTestData.getProductIdThatHasNoPromotion());

        assertTrue(optionalProduct.isPresent());
        assertTrue(optionalProduct.get().getPromotions().isEmpty());
    }

    @Test
    void shouldFindProductWithPromotionById() {
        ProductWithProducerAndPromotionDTO product = productRepository.findById(
                initProductTestData.getProductIdThatHasPromotion(),
                initProductTestData.getDate30DaysAgo()
        );

        assertNotNull(product);
        assertEquals(initProductTestData.getProductIdThatHasPromotion(), product.id());
        assertThat(product.promotionPrice()).isGreaterThan(product.lowestPrice());
    }

    @Test
    void shouldFindProductsBySubcategoryId() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findBySubcategoryId(
                initProductTestData.getSubId(),
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertTrue(optionalPageProducts.isPresent());
        assertPageProducts(optionalPageProducts.get(), 4, 2);

    }

    @Test
    void shouldSearchProductsByEnteredText() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.searchByEnteredText(
                "Product",
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertTrue(optionalPageProducts.isPresent());
        assertPageProducts(optionalPageProducts.get(), 2, 1);
    }

    @Test
    void shouldFindPromotionProducts() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findPromotionProducts(
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertTrue(optionalPageProducts.isPresent());
        assertPageProducts(optionalPageProducts.get(), 3, 3);
    }

    @Test
    void shouldFindNewProducts() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findNewProducts(
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertTrue(optionalPageProducts.isPresent());
        assertPageProducts(optionalPageProducts.get(), 6, 3);
    }

    @Test
    void shouldFindProductByIdWithPromotion() {
        Optional<Product> optionalProduct = productRepository.findWithPromotionById(
                initProductTestData.getProductIdThatHasPromotion()
        );

        assertTrue(optionalProduct.isPresent());
    }

    @Test
    void shouldFindProductsByIdsWithPromotion() {
        List<Product> products = productRepository.findWithPromotionByIds(List.of(
                        initProductTestData.getProductIdThatHasPromotion(),
                        initProductTestData.getProductIdThatHasNoPromotion())
                );

        assertEquals(2, products.size());
    }

    private static void assertPageProducts(Page<ProductWithPromotionDTO> products,
                                           int totalElements,
                                           int totalPromotions) {
        assertEquals(1, products.getTotalPages());
        assertEquals(totalElements, products.getTotalElements());
        assertPageHasPromotionalProduct(products, totalPromotions);
    }

    private static void assertPageHasPromotionalProduct(Page<ProductWithPromotionDTO> products,
                                                        int totalPromotions) {
        List<ProductWithPromotionDTO> productsWithPromotion =
                products.get().filter(p -> p.promotionPrice() != null).toList();
        assertEquals(totalPromotions, productsWithPromotion.size());
    }
}
