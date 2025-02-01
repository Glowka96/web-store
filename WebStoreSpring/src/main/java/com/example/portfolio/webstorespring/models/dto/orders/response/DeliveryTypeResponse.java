package com.example.portfolio.webstorespring.models.dto.orders.response;

import java.math.BigDecimal;

public record DeliveryTypeResponse(Long id,
                                   String name,
                                   BigDecimal price
) {
}
