package com.example.porfolio.webstorespring.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String clazz, String field, String value) {
        super(String.format("%s with %s %s not found", clazz, field, value));
    }
    public ResourceNotFoundException(String clazz, String field, Long value) {
        super(String.format("%s with %s %s not found", clazz, field, value));
    }
    public ResourceNotFoundException(String clazz, String field, Integer value) {
        super(String.format("%s with %s %s not found", clazz, field, value));
    }
}
