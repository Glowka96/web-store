package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.DATE_OF_CREATED;
import static com.example.portfolio.webstorespring.buildhelpers.ProductTypeBuilderHelper.createProductTypeRequest;

public class ProductBuilderHelper {

    private static final String IMAGE_URL = "https://imagetest.pl/image.jpg";

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .dateOfCreation(Timestamp.valueOf(DATE_OF_CREATED))
                .imageUrl(IMAGE_URL)
                .build();
    }

    public static ProductRequest createProductRequest() {
        ProductTypeRequest productTypeRequest = createProductTypeRequest();
        return ProductRequest.builder()
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(20.0))
                .quantity(10L)
                .imageUrl(IMAGE_URL)
                .type(productTypeRequest)
                .build();
    }

    public static ProductRequest createProductRequest(String name, String description, BigDecimal price, Long quantity) {
        return ProductRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .imageUrl(IMAGE_URL)
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
