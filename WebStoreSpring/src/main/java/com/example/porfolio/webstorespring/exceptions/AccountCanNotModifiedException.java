package com.example.porfolio.webstorespring.exceptions;

public class AccountCanNotModifiedException extends RuntimeException {

    public AccountCanNotModifiedException() {
        super(String.format("You can only used or modified your own account!"));
    }
}
