package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ShipmentDto {

    @JsonIgnore
    private Long id;

    private ProductDto productDto;

    private Integer quality;

    private Integer price;

    private OrderDto orderDto;
}
