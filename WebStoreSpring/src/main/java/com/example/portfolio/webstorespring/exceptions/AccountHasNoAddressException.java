package com.example.portfolio.webstorespring.exceptions;

public class AccountHasNoAddressException extends RuntimeException{
    public AccountHasNoAddressException(Long id) {
        super(String.format("Account: %s has no saved address", id));
    }
}
