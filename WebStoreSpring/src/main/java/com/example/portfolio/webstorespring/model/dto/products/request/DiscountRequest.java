package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {


   @DecimalMin(value = "0.1", message = "The discount rate must be greater than or equal to 0.1")
   @DecimalMax(value = "0.90", message = "The discount rate must be less than or equal to 0.90")
   private BigDecimal discountRate;

   @Min(value = 1, message = "The quantity must be greater than or equal to one")
   @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
   private Long quantity;

   private LocalDate endDate;

   @NotEmpty(message = "The list of subcategory names can't be empty")
   private Set<String> subcategoryNames;
}
