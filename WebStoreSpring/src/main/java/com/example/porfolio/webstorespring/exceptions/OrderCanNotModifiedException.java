package com.example.porfolio.webstorespring.exceptions;

public class OrderCanNotModifiedException extends RuntimeException{
    public OrderCanNotModifiedException(String option) {
        super(String.format("The order cannot be %s because the order is being prepared", option));
    }
}
