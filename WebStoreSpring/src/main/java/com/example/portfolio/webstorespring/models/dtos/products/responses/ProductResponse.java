package com.example.portfolio.webstorespring.models.dtos.products.responses;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Set;


public record ProductResponse(Long id,
                              String name,
                              String description,
                              String imageUrl,
                              BigDecimal price,
                              Long quantity,
                              @JsonProperty(value = "productType")
                              ProductTypeResponse productTypeResponse,
                              @JsonProperty(value = "producer")
                              ProducerResponse producerResponse,
                              @JsonProperty(value = "promotions")
                              Set<PromotionResponse> pricePromotionsResponse) {


}
