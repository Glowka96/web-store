package com.example.portfolio.webstorespring.exceptions;

public class DeliveryAddressCanNotEmpty extends RuntimeException {

    public DeliveryAddressCanNotEmpty() {
        super("Delivery address can't be empty.");
    }
}
