package com.example.porfolio.webstorespring.exceptions;

public class OrderCanNotBeUpdated extends Exception{
    public OrderCanNotBeUpdated() {
        super("The order cannot be updated because the order is being prepared");
    }
}
