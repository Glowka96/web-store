package com.example.portfolio.webstorespring.exceptions;

public class AccountCanNotModifiedException extends RuntimeException {

    public AccountCanNotModifiedException() {
        super("You can only used or modified your own account!");
    }
}
