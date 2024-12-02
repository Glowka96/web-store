package com.example.portfolio.webstorespring.model.dto.orders.response;

import java.math.BigDecimal;

public record DeliveryTypeResponse(Long id,
                                   String name,
                                   BigDecimal price
) {
}
