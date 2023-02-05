package com.example.porfolio.webstorespring.model.dto.products;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {

    private Long id;

    @NotNull
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String name;

    private List<SubCategoryDto> subCategoriesDto;
}
