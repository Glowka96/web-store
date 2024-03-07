package com.example.portfolio.webstorespring.exceptions;

public class ShipmentQuantityExceedsProductQuantityException extends RuntimeException {
    public ShipmentQuantityExceedsProductQuantityException() {
        super("The shipment quantity exceeds the product quantity");
    }
}
