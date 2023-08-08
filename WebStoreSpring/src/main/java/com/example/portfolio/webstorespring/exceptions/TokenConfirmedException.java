package com.example.portfolio.webstorespring.exceptions;

public class TokenConfirmedException extends RuntimeException {

    public TokenConfirmedException() {
        super("This token is confirmed.");
    }
}
