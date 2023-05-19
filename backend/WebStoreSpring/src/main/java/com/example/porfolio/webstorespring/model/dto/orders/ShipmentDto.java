package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentDto {

    private Long id;

    @NotNull(message = "The product can't be null")
    @JsonProperty("product")
    private ProductDto productDto;

    private Integer quantity;

    private Double price;

    @JsonIgnore
    private OrderResponse orderResponse;
}
