package com.example.portfolio.webstorespring.model.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private Long quantity;

    @JsonProperty(value = "productType")
    private ProductTypeResponse productTypeResponse;

    @JsonProperty(value = "producer")
    private ProducerResponse producerResponse;

    @JsonProperty(value = "promotions")
    private Set<PromotionResponse> pricePromotionsResponse;
}
