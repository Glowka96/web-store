package com.example.portfolio.webstorespring.exceptions;

public class ShipmentQuantityExceedsProductQuantityException extends RuntimeException {
    public ShipmentQuantityExceedsProductQuantityException(String productNames) {
        super(String.format("The shipment quantity exceeds the available quantity for the following product(s): %s", productNames));
    }
}
