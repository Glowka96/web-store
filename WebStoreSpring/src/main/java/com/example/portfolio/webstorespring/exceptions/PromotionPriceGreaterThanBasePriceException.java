package com.example.portfolio.webstorespring.exceptions;

public class PromotionPriceGreaterThanBasePriceException extends RuntimeException{
    public PromotionPriceGreaterThanBasePriceException() {
        super("The promotion price cannot be greater than the product's base price");
    }
}
