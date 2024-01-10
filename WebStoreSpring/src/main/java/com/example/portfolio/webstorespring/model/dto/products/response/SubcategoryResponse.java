package com.example.portfolio.webstorespring.model.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryResponse {

    private Long id;

    private String name;

    @JsonProperty(value = "products")
    private List<ProductResponse> productResponses;

}
