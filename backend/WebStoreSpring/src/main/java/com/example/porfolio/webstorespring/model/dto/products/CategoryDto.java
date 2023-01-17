package com.example.porfolio.webstorespring.model.dto.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {

    @JsonIgnore
    private Long id;

    private String name;

    private List<SubCategoryDto> subCategoriesDto;
}
