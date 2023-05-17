package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentDto {

    private Long id;

    @NotNull(message = "The product can't be null")
    @NotBlank(message = "The product can't be blank")
    @JsonProperty("product")
    private ProductDto productDto;

    @NotNull(message = "The quantity can't be null")
    @NotBlank(message = "The quantity can't be blank")
    private Integer quantity;

    private Double price;

    @JsonIgnore
    private OrderResponse orderResponse;
}
