package com.example.portfolio.webstorespring.model.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPricePromotionResponse {

    private Long id;

    @JsonProperty(value = "product")
    private ProductResponse productResponse;

    private BigDecimal promotionPrice;

    private Date startDate;

    private Date endDate;
}
