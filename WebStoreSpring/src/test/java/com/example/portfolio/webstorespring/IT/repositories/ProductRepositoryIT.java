package com.example.portfolio.webstorespring.IT.repositories;

import com.example.portfolio.webstorespring.IT.ContainersConfig;
import com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.products.*;
import com.example.portfolio.webstorespring.repositories.products.*;
import com.natpryce.makeiteasy.Maker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.ID;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.PRICE;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;


@Import(ContainersConfig.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ProductRepositoryIT {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private ProductPricePromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;

    private Long productId;
    private Long subId;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    private final LocalDateTime date30DaysAgo = zonedDateTime.minusDays(30).toLocalDateTime();

    @BeforeEach
    void init() {
        deleteAllInDatabase();

        Subcategory subcategory = subcategoryRepository.save(
                SubcategoryBuilderHelper.createSubcategory());
        Subcategory subcategory2 = subcategoryRepository.save(
                SubcategoryBuilderHelper.createSubcategory());
        ProductType productType = productTypeRepository.save(
                ProductTypeBuilderHelper.createProductType());
        Producer producer = producerRepository.save(
                ProducerBuilderHelper.createProducer());

        ProductPricePromotion currencyPromotion = getCurrencyPromotion();
        ProductPricePromotion currencyPromotion2 = getCurrencyPromotion();
        ProductPricePromotion expiredPromotionWithLowestPriceLast30Days = make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID))
                .but(with(PRICE, BigDecimal.valueOf(5.00)))
                .but(with(START_DATE, zonedDateTime.minusDays(20).toLocalDateTime()))
                .but(with(END_DATE, zonedDateTime.minusDays(10).toLocalDateTime())));
        ProductPricePromotion expiredPromotion = make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID)));

        Maker<Product> productMaker = a(BASIC_PRODUCT)
                .but(withNull(ID))
                .but(with(SUBCATEGORY, subcategory))
                .but(with(PRODUCT_TYPE, productType))
                .but(with(PRODUCER, producer))
                .but(with(PRICE_PROMOTIONS, Set.of()));

        Product productWithoutPromotion = productRepository.save(make(productMaker));
        Product productWithCurrencyPromotion = productRepository.save(make(productMaker));
        Product productWithExpiredPromotion = productRepository.save(make(productMaker));
        Product productWithCurrencyPromotionAndOtherSubcategory = productRepository.save(
                make(productMaker.but(with(NAME, "Product"))
                        .but(with(SUBCATEGORY, subcategory2))));

        expiredPromotionWithLowestPriceLast30Days.setProduct(productWithCurrencyPromotion);
        currencyPromotion.setProduct(productWithCurrencyPromotion);
        expiredPromotion.setProduct(productWithExpiredPromotion);
        currencyPromotion2.setProduct(productWithCurrencyPromotionAndOtherSubcategory);

        promotionRepository.save(expiredPromotionWithLowestPriceLast30Days);
        promotionRepository.save(currencyPromotion);
        promotionRepository.save(expiredPromotion);
        promotionRepository.save(currencyPromotion2);

        subId = productWithoutPromotion.getSubcategory().getId();
        productId = productWithCurrencyPromotion.getId();
    }

    @Test
    void shouldFindProductById() {
        ProductWithProducerAndPromotionDTO product =
                productRepository.findProductById(productId, date30DaysAgo);
        assertThat(product).isNotNull();
        assertThat(product.id()).isEqualTo(productId);
        assertThat(product.promotionPrice()).isGreaterThan(product.lowestPrice());
    }

    @Test
    void shouldFindProductsBySubcategoryId() {
        Pageable pageable = createPageable();
        Optional<Page<ProductWithPromotionDTO>> products =
                productRepository.findProductsBySubcategoryId(subId, date30DaysAgo, pageable);

        assertThat(products).isPresent();
        assertProductsIfPresent(products, 3, 1);

    }

    @Test
    void shouldSearchProductsByEnteredText() {
        Pageable pageable = createPageable();
        Optional<Page<ProductWithPromotionDTO>> products =
                productRepository.searchProductsByEnteredText("Product", date30DaysAgo, pageable);

        assertThat(products).isPresent();
        assertProductsIfPresent(products, 1, 1);
    }

    @Test
    void shouldFindPromotionProducts() {
        Pageable pageable = createPageable();
        Optional<Page<ProductWithPromotionDTO>> products =
                productRepository.findPromotionProducts(date30DaysAgo, pageable);

        assertThat(products).isPresent();
        assertProductsIfPresent(products, 2, 2);
    }

    @Test
    void shouldFindNewProducts() {
        Pageable pageable = createPageable();
        Optional<Page<ProductWithPromotionDTO>> products =
                productRepository.findNewProducts(date30DaysAgo, pageable);

        assertThat(products).isPresent();
        assertProductsIfPresent(products, 4, 2);
    }

    private void deleteAllInDatabase() {
        subcategoryRepository.deleteAll();
        productTypeRepository.deleteAll();
        producerRepository.deleteAll();
        promotionRepository.deleteAll();
        productRepository.deleteAll();
    }

    private ProductPricePromotion getCurrencyPromotion() {
        return make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID))
                .but(with(START_DATE, zonedDateTime.minusDays(1).toLocalDateTime()))
                .but(with(END_DATE, zonedDateTime.plusDays(1).toLocalDateTime())));
    }

    private  Pageable createPageable() {
        return PageRequest.of(0, 12, Sort.by(Sort.Direction.fromString("asc"), "id"));
    }

    private static void assertProductsIfPresent(Optional<Page<ProductWithPromotionDTO>> products,
                                                int totalElements,
                                                int totalPromotions) {
        products.ifPresent((page) -> {
            assertThat(page.getTotalPages()).isEqualTo(1);
            assertThat(page.getTotalElements()).isEqualTo(totalElements);
            assertPageHasPromotionalProduct(page, totalPromotions);
        });
    }

    private static void assertPageHasPromotionalProduct(Page<ProductWithPromotionDTO> products,
                                                        int totalPromotions) {
        List<ProductWithPromotionDTO> productsWithPromotion =
                products.get().filter(p -> p.promotionPrice() != null).toList();
        assertThat(productsWithPromotion).hasSize(totalPromotions);
    }
}
