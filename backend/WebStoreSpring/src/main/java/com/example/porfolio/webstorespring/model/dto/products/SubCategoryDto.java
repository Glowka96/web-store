package com.example.porfolio.webstorespring.model.dto.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class SubCategoryDto {

    @JsonIgnore
    private Long id;

    private String name;

    @JsonIgnore
    private CategoryDto categoryDto;

    private List<ProductDto> productsDto;

}
