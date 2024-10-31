package com.example.portfolio.webstorespring.exceptions;

public class ShipmentQuantityExceedsProductQuantityException extends RuntimeException {
    public ShipmentQuantityExceedsProductQuantityException(Integer quantity, String productName) {
        super(String.format("The shipment quantity %d exceeds the available quantity for product: %s", quantity, productName));

    }
}
