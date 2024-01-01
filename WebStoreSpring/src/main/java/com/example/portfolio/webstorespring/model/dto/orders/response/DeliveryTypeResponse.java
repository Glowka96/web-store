package com.example.portfolio.webstorespring.model.dto.orders.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryTypeResponse {

    private Long id;

    private String type;

    private BigDecimal price;
}
