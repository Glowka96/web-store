package com.example.portfolio.webstorespring.models.dto.products.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionRequest(
        @NotNull(message = "The product id can't be null")
        Long productId,

        @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
        @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999.99")
        BigDecimal promotionPrice,

        @PastOrPresent(message = "The start date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startDate,

        @Future(message = "The end date must be a future date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endDate
) {
}
