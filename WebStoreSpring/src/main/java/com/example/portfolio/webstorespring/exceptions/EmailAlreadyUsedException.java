package com.example.portfolio.webstorespring.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(String.format("This email %s is already in use.", email));
    }
}
