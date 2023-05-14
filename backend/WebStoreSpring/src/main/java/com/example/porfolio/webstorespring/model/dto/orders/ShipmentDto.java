package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentDto {

    private Long id;

    @NotNull
    @JsonProperty("product")
    private ProductDto productDto;

    @NotNull
    private Integer quantity;

    private Double price;

    @JsonIgnore
    private OrderResponse orderResponse;
}
