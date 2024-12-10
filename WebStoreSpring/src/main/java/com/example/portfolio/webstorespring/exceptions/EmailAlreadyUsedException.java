package com.example.portfolio.webstorespring.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("This email is already in use.");
    }
}
