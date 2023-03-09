package com.example.porfolio.webstorespring.exceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException() {
        super("Email already confirmed");
    }
}
