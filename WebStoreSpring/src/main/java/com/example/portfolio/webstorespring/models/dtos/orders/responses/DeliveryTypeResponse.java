package com.example.portfolio.webstorespring.models.dtos.orders.responses;

import java.math.BigDecimal;

public record DeliveryTypeResponse(Long id,
                                   String name,
                                   BigDecimal price
) {
}
