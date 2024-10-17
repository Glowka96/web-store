package com.example.portfolio.webstorespring.exceptions;

public class DiscountIsInvalid extends RuntimeException {
    public DiscountIsInvalid() {
        super("Not found discount or this discount is disable.");
    }
}
