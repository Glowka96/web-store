package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.createProductPricePromotion;

public class ProductBuilderHelper {

    private static final String IMAGE_URL = "https://imagetest.pl/image.jpg";

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .dateOfCreation(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .pricePromotions(Set.of())
                .imageUrl(IMAGE_URL)
                .build();
    }

    public static Product createProductWithPromotion() {
        ProductPricePromotion pricePromotion = createProductPricePromotion();
        return Product.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .dateOfCreation(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .pricePromotions(Set.of(pricePromotion))
                .imageUrl(IMAGE_URL)
                .build();
    }

    public static ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .imageUrl(IMAGE_URL)
                .productTypeId(1L)
                .build();
    }

    public static ProductRequest createProductRequest(String name, String description, BigDecimal price, Long quantity) {
        return ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .imageUrl(IMAGE_URL)
                .productTypeId(1L)
                .build();
    }

    public static ProductResponse createProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .imageUrl(IMAGE_URL)
                .build();
    }
}
