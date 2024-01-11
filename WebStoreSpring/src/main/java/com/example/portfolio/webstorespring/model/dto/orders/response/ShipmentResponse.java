package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;

    @JsonProperty(value = "product")
    private ProductResponse productResponse;

    private Integer quantity;

    private BigDecimal price;
}
