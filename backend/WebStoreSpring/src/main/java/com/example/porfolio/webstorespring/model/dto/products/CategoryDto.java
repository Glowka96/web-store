package com.example.porfolio.webstorespring.model.dto.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {

    @JsonIgnore
    private Long id;

    @Size(min=2, message = "Min letters: 2")
    private String name;

    private List<SubCategoryDto> subCategoriesDto;
}
