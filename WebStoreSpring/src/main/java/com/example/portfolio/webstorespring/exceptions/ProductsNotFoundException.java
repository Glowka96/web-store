package com.example.portfolio.webstorespring.exceptions;

import java.util.List;

public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException(List<Long> missingIds) {
        super(String.format("Product not found with id %d", missingIds));
    }
}
