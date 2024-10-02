package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.*;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper.BASIC_PROMOTION;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ProductBuilderHelper {

    public static final Property<Product, Long> ID = new Property<>();
    public static final Property<Product, String> NAME = new Property<>();
    public static final Property<Product, String> DESCRIPTION = new Property<>();
    public static final Property<Product, String> IMAGE_URL = new Property<>();
    public static final Property<Product, BigDecimal> PRICE = new Property<>();
    public static final Property<Product, Long> QUANTITY = new Property<>();
    public static final Property<Product, ProductType> PRODUCT_TYPE = new Property<>();
    public static final Property<Product, Subcategory> SUBCATEGORY = new Property<>();
    public static final Property<Product, Producer> PRODUCER = new Property<>();
    public static final Property<Product, LocalDateTime> CREATED_AT = new Property<>();
    public static final Property<Product, Set<Promotion>> PRICE_PROMOTIONS = new Property<>();

    private static final String PRODUCT_NAME = "Test";
    private static final String PRODUCT_DESCRIPTION = "Test description";
    private static final String PRODUCT_IMAGE_URL = "https://imagetest.pl/image.jpg";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(20.0);
    private static final Long PRODUCT_QUANTITY = 10L;

    public static final Instantiator<Product> BASIC_PRODUCT = lookup ->
            Product.builder()
            .id(lookup.valueOf(ID,1L))
            .name(lookup.valueOf(NAME, PRODUCT_NAME))
            .description(lookup.valueOf(DESCRIPTION, PRODUCT_DESCRIPTION))
            .imageUrl(lookup.valueOf(IMAGE_URL, PRODUCT_IMAGE_URL))
            .price(lookup.valueOf(PRICE, PRODUCT_PRICE))
            .quantity(lookup.valueOf(QUANTITY,PRODUCT_QUANTITY))
            .type(lookup.valueOf(PRODUCT_TYPE, ProductTypeBuilderHelper.createProductType()))
            .subcategory(lookup.valueOf(SUBCATEGORY, SubcategoryBuilderHelper.createSubcategory()))
            .producer(lookup.valueOf(PRODUCER, ProducerBuilderHelper.createProducer()))
            .createdAt(lookup.valueOf(CREATED_AT, LOCAL_DATE_TIME))
            .promotions(lookup.valueOf(PRICE_PROMOTIONS, Set.of(make(a(BASIC_PROMOTION)))))
            .build();

    public static ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .quantity(PRODUCT_QUANTITY)
                .imageUrl(PRODUCT_IMAGE_URL)
                .productTypeId(1L)
                .build();
    }

    public static ProductRequest createProductRequest(String name,
                                                      String description,
                                                      BigDecimal price,
                                                      Long quantity) {
        return ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .imageUrl(PRODUCT_IMAGE_URL)
                .productTypeId(1L)
                .build();
    }

    public static ProductRequest createProductRequest(String name,
                                                      String description,
                                                      BigDecimal price,
                                                      String imageUrl,
                                                      Long quantity,
                                                      Long productTypeId) {
        return ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .imageUrl(imageUrl)
                .productTypeId(productTypeId)
                .build();
    }

    public static ProductResponse createProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .quantity(PRODUCT_QUANTITY)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();
    }
}
