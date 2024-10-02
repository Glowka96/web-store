package com.example.portfolio.webstorespring.model.dto.products.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionRequesst {

    @NotNull(message = "The product id can't be null")
    @Positive(message = "The product id must be positive number")
    private Long productId;

    @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
    @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999999.99")
    private BigDecimal promotionPrice;

    @PastOrPresent(message = "The start date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Future(message = "The end date must be a future date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
