package com.example.portfolio.webstorespring.model.dto.products.response;


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
