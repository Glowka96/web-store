package com.example.portfolio.webstorespring.model.dto.products;

public record PageProductsOptions(Integer pageNo,
                                  Integer size,
                                  String sortOption) {
}
