package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentDto {

    private Long id;

    @NotNull
    private ProductDto productDto;

    @NotNull
    private Integer quality;

    private Double price;

    private OrderDto orderDto;
}
