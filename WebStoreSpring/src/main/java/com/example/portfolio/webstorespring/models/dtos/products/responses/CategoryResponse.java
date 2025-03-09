package com.example.portfolio.webstorespring.models.dtos.products.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryResponse(Long id,

                               String name,

                               @JsonProperty(value = "subcategories")
                               List<SubcategoryResponse> subcategoryResponses) {

}
