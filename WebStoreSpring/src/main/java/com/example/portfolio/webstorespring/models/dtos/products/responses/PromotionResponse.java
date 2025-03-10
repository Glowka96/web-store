package com.example.portfolio.webstorespring.models.dtos.products.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionResponse(Long id,
                                @JsonProperty(value = "product")
                                ProductResponse productResponse,

                                BigDecimal promotionPrice,

                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                LocalDateTime startDate,

                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                LocalDateTime endDate) {
}
