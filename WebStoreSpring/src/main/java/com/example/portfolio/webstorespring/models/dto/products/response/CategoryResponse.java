package com.example.portfolio.webstorespring.models.dto.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryResponse(Long id,

                               String name,

                               @JsonProperty(value = "subcategories")
                               List<SubcategoryResponse> subcategoryResponses) {

}
