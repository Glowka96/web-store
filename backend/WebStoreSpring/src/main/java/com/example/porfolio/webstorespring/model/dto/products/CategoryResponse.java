package com.example.porfolio.webstorespring.model.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    private Long id;

    @NotNull(message = "The name can't be null")
    @NotBlank(message = "The name can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String name;

    @JsonProperty("subcategories")
    private List<SubcategoryRequest> subcategoriesDto;
}
