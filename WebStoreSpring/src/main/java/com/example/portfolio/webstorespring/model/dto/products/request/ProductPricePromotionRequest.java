package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductPricePromotionRequest {

    @NotNull(message = "The product id can't be null")
    @NotBlank(message = "The product id can't be blank")
    private Long productId;

    @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
    @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999999.99")
    private BigDecimal promotionPrice;

    @PastOrPresent(message = "The start date must be in the past or present")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @Future(message = "The end date must be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
