package com.example.portfolio.webstorespring.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private static final String MESSAGE = "%s with %s %s not found";
    public ResourceNotFoundException(String clazz, String field, String value) {
        super(String.format(MESSAGE , clazz, field, value));
    }
    public ResourceNotFoundException(String clazz, String field, Long value) {
        super(String.format(MESSAGE, clazz, field, value));
    }
    public ResourceNotFoundException(String clazz, String field, Integer value) {
        super(String.format(MESSAGE, clazz, field, value));
    }
}
