package com.example.portfolio.webstorespring.exceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException() {
        super("Email already confirmed.");
    }
}
