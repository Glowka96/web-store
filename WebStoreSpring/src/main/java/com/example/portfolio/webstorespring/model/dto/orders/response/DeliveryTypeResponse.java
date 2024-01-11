package com.example.portfolio.webstorespring.model.dto.orders.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeResponse {

    private Long id;

    private String type;

    private BigDecimal price;
}
