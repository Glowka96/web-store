package com.example.porfolio.webstorespring.model.dto.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CategoryRequest {

    @NotNull(message = "The name can't be null")
    @NotBlank(message = "The name can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String name;
}