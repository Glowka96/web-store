package com.example.portfolio.webstorespring.model.dto.products;

import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private Double price;

    private ProductType type;

    @JsonProperty("producer")
    private ProducerResponse producerResponse;
}
