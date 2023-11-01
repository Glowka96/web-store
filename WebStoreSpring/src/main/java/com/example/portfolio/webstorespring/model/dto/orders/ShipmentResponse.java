package com.example.portfolio.webstorespring.model.dto.orders;

import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShipmentResponse {

    private Long id;

    @JsonProperty("product")
    private ProductResponse productResponse;

    private Integer quantity;

    private BigDecimal price;
}
