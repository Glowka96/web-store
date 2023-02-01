package com.example.porfolio.webstorespring.model.dto.products;

import com.example.porfolio.webstorespring.model.entity.products.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ProductDto {

    @JsonIgnore
    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private Double price;

    private ProductType type;

    @JsonIgnore
    private SubCategoryDto subCategoryDto;

    private ProducerDto producerDto;
}
