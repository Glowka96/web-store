package com.example.porfolio.webstorespring.model.dto.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ProducerDto {

    private final Long id;

    private final String name;

    private final List<ProductDto> productsDto;
}
