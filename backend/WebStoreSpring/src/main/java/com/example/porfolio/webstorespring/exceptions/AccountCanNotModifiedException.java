package com.example.porfolio.webstorespring.exceptions;

public class AccountCanNotModifiedException extends RuntimeException {

    public AccountCanNotModifiedException() {
        super(String.format("You can only modified your own account!"));
    }
}
