package com.example.portfolio.webstorespring.model.dto.orders.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeRequest {

    @NotBlank(message = "The delivery type can't be blank")
    @Size(min=3, max = 20, message = "The delivery type must between min 3 and max 20 letters")
    private String type;

    @DecimalMin(value = "0.00", message = "The price must be greater than or equal to 0.00")
    @DecimalMax(value = "999.99", message = "The price must be less than or equal to 999.99")
    private BigDecimal price;

}
