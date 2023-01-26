package com.example.porfolio.webstorespring.exceptions;

public enum ErrorMSG {
    CATEGORY_WITH_ID_NOT_FOUND("Category with id %s not found"),
    ACCOUNT_NOT_FOUND("Account with email %s not found");
    private final String message;
    ErrorMSG(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
