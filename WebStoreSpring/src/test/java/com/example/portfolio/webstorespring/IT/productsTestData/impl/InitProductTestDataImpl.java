package com.example.portfolio.webstorespring.IT.productsTestData.impl;

import com.example.portfolio.webstorespring.IT.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.products.*;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.ID;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.PRICE;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductType;
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
    private ProductPricePromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Getter
    private Long productId;
    @Getter
    private Long subId;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    @Getter
    private final LocalDateTime date30DaysAgo = zonedDateTime.minusDays(30).toLocalDateTime();

    public void initTestData() {
        Subcategory subcategory = subcategoryRepository.save(createSubcategory("Puzzle"));
        Subcategory subcategory2 = subcategoryRepository.save(createSubcategory("Test"));
        ProductType productType = productTypeRepository.save(createProductType("Test"));
        ProductType productType2 = productTypeRepository.save(createProductType("Education"));
        Producer producer = producerRepository.save(createProducer("Test"));
        Producer producer2 = producerRepository.save(createProducer("Producer"));

        Maker<Product> productMaker = getProductMaker(subcategory, productType, producer);
        Maker<Product> productMaker2 = getProductMaker(subcategory, productType2, producer2);

        ProductPricePromotion currencyPromotion = getCurrencyPromotion();
        ProductPricePromotion currencyPromotion2 = getCurrencyPromotion();
        ProductPricePromotion currencyPromotion3 = getCurrencyPromotion();
        ProductPricePromotion expiredPromotionWithLowestPriceLast30Days = getExpiredPromotionWithLowestPriceLast30Days();
        ProductPricePromotion expiredPromotion = make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID)));

        Product productWithoutPromotion = productRepository.save(make(productMaker));
        Product productWithPromotion = productRepository.save(make(productMaker));
        Product productWithExpiredPromotion = productRepository.save(make(productMaker));
        Product productWithPromotionAndOtherSubcategory = productRepository.save(
                make(productMaker.but(with(NAME, "Product"))
                        .but(with(SUBCATEGORY, subcategory2))));
        Product productWithPromotionAndOtherProductTypeAndProducer = productRepository.save(make(productMaker2));

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

        subId = productWithoutPromotion.getSubcategory().getId();
        productId = productWithPromotion.getId();
    }

    public void deleteTestData() {
        subcategoryRepository.deleteAll();
        productTypeRepository.deleteAll();
        producerRepository.deleteAll();
        promotionRepository.deleteAll();
        productRepository.deleteAll();
    }

    public Pageable getPageable() {
        return PageRequest.of(0, 12, Sort.by(Sort.Direction.fromString("asc"), "name"));
    }

    private ProductPricePromotion getCurrencyPromotion() {
        return make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID))
                .but(with(START_DATE, zonedDateTime.minusDays(10).toLocalDateTime()))
                .but(with(END_DATE, zonedDateTime.plusDays(50).toLocalDateTime())));
    }

    private ProductPricePromotion getExpiredPromotionWithLowestPriceLast30Days() {
        return make(a(BASIC_PROMOTION)
                .but(withNull(ProductPricePromotionBuilderHelper.ID))
                .but(with(PRICE, BigDecimal.valueOf(5.00)))
                .but(with(START_DATE, zonedDateTime.minusDays(20).toLocalDateTime()))
                .but(with(END_DATE, zonedDateTime.minusDays(10).toLocalDateTime())));
    }

    @NotNull
    private static Maker<Product> getProductMaker(Subcategory subcategory, ProductType productType, Producer producer) {
        return a(BASIC_PRODUCT)
                .but(withNull(ID))
                .but(with(SUBCATEGORY, subcategory))
                .but(with(PRODUCT_TYPE, productType))
                .but(with(PRODUCER, producer))
                .but(with(PRICE_PROMOTIONS, Set.of()));
    }
}
