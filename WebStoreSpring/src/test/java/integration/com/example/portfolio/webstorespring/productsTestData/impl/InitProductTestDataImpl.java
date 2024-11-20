package com.example.portfolio.webstorespring.productsTestData.impl;

import com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.products.*;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.repositories.products.*;
import com.natpryce.makeiteasy.Maker;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducerWithoutId;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeWithoutId;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategory;
import static com.natpryce.makeiteasy.MakeItEasy.*;

public class InitProductTestDataImpl implements InitProductTestData {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Getter
    private Long productIdThatHasPromotion;
    @Getter
    private Long productIdThatHasNoPromotion;
    @Getter
    private Long productIdThatHasOtherSubcategoryAndNoPromotion;
    @Getter
    private Long subId;
    @Getter
    private Long producerId;
    @Getter
    private Long productTypeId;
    private Subcategory subcategory;
    private Producer producer;
    private ProductType productType;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    @Getter
    private final LocalDateTime date30DaysAgo = zonedDateTime.minusDays(30).toLocalDateTime();

    public void initTestData() {
        initOneProduct();
        Subcategory subcategory2 = subcategoryRepository.save(createSubcategory("Test"));
        ProductType productType2 = productTypeRepository.save(createProductTypeWithoutId("Education"));
        Producer producer2 = producerRepository.save(createProducerWithoutId("Producer"));

        Maker<Product> productMaker = getProductMaker(subcategory, productType, producer);
        Maker<Product> productMaker2 = getProductMaker(subcategory, productType2, producer2);
        Maker<Product> productMaker3 = getProductMaker(subcategory2, productType, producer);

        Promotion currencyPromotion = getCurrencyPromotion();
        Promotion currencyPromotion2 = getCurrencyPromotion();
        Promotion currencyPromotion3 = getCurrencyPromotion();
        Promotion expiredPromotionWithLowestPriceLast30Days = getExpiredPromotionWithLowestPriceLast30Days();
        Promotion expiredPromotion = make(a(PromotionBuilderHelper.BASIC_PROMOTION)
                .but(withNull(PromotionBuilderHelper.ID)));

        Product productWithPromotion = productRepository.save(
                make(productMaker.but(with(PRICE, BigDecimal.valueOf(60L))))
        );
        Product productWithExpiredPromotion = productRepository.save(make(productMaker));
        Product productWithPromotionAndOtherSubcategory = productRepository.save(
                make(productMaker3.but(with(NAME, "Product")))
        );
        Product productWithPromotionAndOtherProductTypeAndProducer = productRepository.save(make(productMaker2));
        Product productWithOtherSubcategoryAndNoPromotion = productRepository.save(
                make(productMaker3.but(with(NAME, "Product 2")))
        );

        expiredPromotionWithLowestPriceLast30Days.setProduct(productWithPromotion);
        currencyPromotion.setProduct(productWithPromotion);
        expiredPromotion.setProduct(productWithExpiredPromotion);
        currencyPromotion2.setProduct(productWithPromotionAndOtherSubcategory);
        currencyPromotion3.setProduct(productWithPromotionAndOtherProductTypeAndProducer);

        promotionRepository.save(expiredPromotionWithLowestPriceLast30Days);
        promotionRepository.save(currencyPromotion);
        promotionRepository.save(expiredPromotion);
        promotionRepository.save(currencyPromotion2);
        promotionRepository.save(currencyPromotion3);

        productIdThatHasPromotion = productWithPromotion.getId();
        productIdThatHasOtherSubcategoryAndNoPromotion = productWithOtherSubcategoryAndNoPromotion.getId();
    }

    public void initOneProduct() {
        subcategory = subcategoryRepository.save(createSubcategory("Puzzle"));
        productType = productTypeRepository.save(createProductTypeWithoutId("Test"));
        producer = producerRepository.save(createProducerWithoutId("Test"));

        subId = subcategory.getId();
        producerId = producer.getId();
        productTypeId = productType.getId();

        Product product = productRepository.save(make(getProductMaker(subcategory, productType, producer)));
        productIdThatHasNoPromotion = product.getId();
    }

    public void deleteTestData() {
        productRepository.deleteAll();
        subcategoryRepository.deleteAll();
        productTypeRepository.deleteAll();
        producerRepository.deleteAll();
    }

    public Pageable getPageable() {
        return PageRequest.of(0, 12, Sort.by(Sort.Direction.fromString("asc"), "name"));
    }

    private Promotion getCurrencyPromotion() {
        return make(a(PromotionBuilderHelper.BASIC_PROMOTION)
                .but(withNull(PromotionBuilderHelper.ID))
                .but(with(PromotionBuilderHelper.START_DATE, zonedDateTime.minusDays(10).toLocalDateTime()))
                .but(with(PromotionBuilderHelper.END_DATE, zonedDateTime.plusDays(50).toLocalDateTime())));
    }

    private Promotion getExpiredPromotionWithLowestPriceLast30Days() {
        return make(a(PromotionBuilderHelper.BASIC_PROMOTION)
                .but(withNull(PromotionBuilderHelper.ID))
                .but(with(PromotionBuilderHelper.PRICE, BigDecimal.valueOf(5.00)))
                .but(with(PromotionBuilderHelper.START_DATE, zonedDateTime.minusDays(20).toLocalDateTime()))
                .but(with(PromotionBuilderHelper.END_DATE, zonedDateTime.minusDays(10).toLocalDateTime())));
    }

    @NotNull
    private Maker<Product> getProductMaker(Subcategory subcategory, ProductType productType, Producer producer) {
        return a(BASIC_PRODUCT)
                .but(withNull(ID))
                .but(with(SUBCATEGORY, subcategory))
                .but(with(PRODUCT_TYPE, productType))
                .but(with(PRODUCER, producer))
                .but(with(PRICE_PROMOTIONS, Set.of()))
                .but(with(CREATED_AT, zonedDateTime.toLocalDateTime()));
    }
}
