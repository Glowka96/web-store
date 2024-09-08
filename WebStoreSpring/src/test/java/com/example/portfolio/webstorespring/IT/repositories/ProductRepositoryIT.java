package com.example.portfolio.webstorespring.IT.repositories;

import com.example.portfolio.webstorespring.IT.ContainersConfig;
import com.example.portfolio.webstorespring.IT.productsTestData.InitProductConfig;
import com.example.portfolio.webstorespring.IT.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Import({ContainersConfig.class, InitProductConfig.class})
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
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

    @AfterEach
    void delete() {
        initProductTestData.deleteTestData();
    }

    @Test
    void shouldFindProductById() {
        ProductWithProducerAndPromotionDTO product = productRepository.findProductById(
                initProductTestData.getProductId(),
                initProductTestData.getDate30DaysAgo()
        );

        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(initProductTestData.getProductId());
        assertThat(product.promotionPrice()).isGreaterThan(product.lowestPrice());
    }

    @Test
    void shouldFindProductsBySubcategoryId() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findProductsBySubcategoryId(
                initProductTestData.getSubId(),
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertThat(optionalPageProducts).isPresent();
        assertPageProducts(optionalPageProducts.get(), 4, 2);

    }

    @Test
    void shouldSearchProductsByEnteredText() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.searchProductsByEnteredText(
                "Product",
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertThat(optionalPageProducts).isPresent();
        assertPageProducts(optionalPageProducts.get(), 1, 1);
    }

    @Test
    void shouldFindPromotionProducts() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findPromotionProducts(
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertThat(optionalPageProducts).isPresent();
        assertPageProducts(optionalPageProducts.get(), 3, 3);
    }

    @Test
    void shouldFindNewProducts() {
        Optional<Page<ProductWithPromotionDTO>> optionalPageProducts = productRepository.findNewProducts(
                initProductTestData.getDate30DaysAgo(),
                initProductTestData.getPageable()
        );

        assertThat(optionalPageProducts).isPresent();
        assertPageProducts(optionalPageProducts.get(), 5, 3);
    }

    @Test
    void shouldFindProductByIdWithPromotion() {
        Optional<Product> optionalProduct = productRepository.findProductByIdWithPromotion(
                initProductTestData.getProductId()
        );

        assertThat(optionalProduct).isPresent();
    }

    @Test
    void shouldFindProductsByIdsWithPromotion() {
        List<Product> products = productRepository.findProductsByIdsWithPromotion(List.of(
                        initProductTestData.getProductId(),
                        initProductTestData.getProductId() - 1)
                );

        assertThat(products).hasSize(2);
    }

    private static void assertPageProducts(Page<ProductWithPromotionDTO> products,
                                           int totalElements,
                                           int totalPromotions) {
        assertThat(products.getTotalPages()).isEqualTo(1);
        assertThat(products.getTotalElements()).isEqualTo(totalElements);
        assertPageHasPromotionalProduct(products, totalPromotions);
    }

    private static void assertPageHasPromotionalProduct(Page<ProductWithPromotionDTO> products,
                                                        int totalPromotions) {
        List<ProductWithPromotionDTO> productsWithPromotion =
                products.get().filter(p -> p.promotionPrice() != null).toList();
        assertThat(productsWithPromotion).hasSize(totalPromotions);
    }
}
