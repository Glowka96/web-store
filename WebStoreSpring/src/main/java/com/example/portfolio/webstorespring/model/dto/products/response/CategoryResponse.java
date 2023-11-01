package com.example.portfolio.webstorespring.model.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    private Long id;

    private String name;

    @JsonProperty(value = "subcategories")
    private List<SubcategoryResponse> subcategoryResponses;
}
