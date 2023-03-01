package com.example.porfolio.webstorespring.exceptions;

public class AccountCanNotModifiedException extends RuntimeException {

    public AccountCanNotModifiedException(String option) {
        super(String.format("You can only %s your own account!", option));
    }
}
