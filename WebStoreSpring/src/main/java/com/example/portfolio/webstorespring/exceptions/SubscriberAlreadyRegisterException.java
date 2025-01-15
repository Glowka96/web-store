package com.example.portfolio.webstorespring.exceptions;

public class SubscriberAlreadyRegisterException extends RuntimeException {
    public SubscriberAlreadyRegisterException(String email) {
        super(String.format("This email: %s is already subscribed", email));
    }
}
