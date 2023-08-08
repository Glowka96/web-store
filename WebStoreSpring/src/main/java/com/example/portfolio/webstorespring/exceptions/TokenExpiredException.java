package com.example.portfolio.webstorespring.exceptions;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("This token is expired.");
    }
}
