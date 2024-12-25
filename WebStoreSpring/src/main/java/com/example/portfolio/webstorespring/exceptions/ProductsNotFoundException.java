package com.example.portfolio.webstorespring.exceptions;

import java.util.List;

public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException(List<Long> missingIds) {
        super(String.format("Products with ids %s not found.", missingIds));
    }
}
