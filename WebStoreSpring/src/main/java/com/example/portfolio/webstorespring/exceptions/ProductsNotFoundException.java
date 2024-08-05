package com.example.portfolio.webstorespring.exceptions;

public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException() {
        super("One or more products could not be found");
    }
}
