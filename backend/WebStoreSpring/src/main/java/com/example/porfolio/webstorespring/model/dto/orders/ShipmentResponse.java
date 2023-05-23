package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductResponse;
import lombok.Data;

@Data
public class ShipmentResponse {

    private Long id;

    private ProductResponse productResponse;

    private Integer quantity;

    private Double price;
}
