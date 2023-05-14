package com.example.porfolio.webstorespring.model.dto.products;

import com.example.porfolio.webstorespring.model.entity.products.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto {

    private Long id;

    @NotNull(message = "The name can't be null")
    @NotBlank(message = "The name can't be blank")
    @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String name;

    @NotNull(message = "The description can't be null")
    @NotBlank(message = "The description can't be blank")
    @Size(min = 3, max = 256, message = "The description must between min 3 and max 256 letters")
    private String description;

    @NotNull(message = "The image url can't be null")
    @NotBlank(message = "The image url can't be blank")
    private String imageUrl;

    private Double price;

    private ProductType type;
    @JsonIgnore
    private SubcategoryDto subcategoryDto;
    @JsonIgnore
    private ProducerDto producerDto;
}
