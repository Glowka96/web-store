package com.example.portfolio.webstorespring.model.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubcategoryResponse {

    private Long id;

    private String name;

    @JsonProperty(value = "products")
    private List<ProductResponse> productResponses;

}
