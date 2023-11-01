package com.example.portfolio.webstorespring.model.dto.products.response;

import com.example.portfolio.webstorespring.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private Long quantity;

    private ProductType type;

    @JsonProperty(value = "producer")
    private ProducerResponse producerResponse;
}
