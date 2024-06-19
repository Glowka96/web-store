package com.example.portfolio.webstorespring.exceptions;

public class AccountHasNoAddressException extends RuntimeException{
    public AccountHasNoAddressException() {
        super("Account has no saved address");
    }
}
