package com.example.porfolio.webstorespring.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String from, String what, String value) {
        super(String.format("%s with %s %s not found", from, what, value));
    }
    public ResourceNotFoundException(String from, String what, Long value) {
        super(String.format("%s with %s %s not found", from, what, value));
    }
}
