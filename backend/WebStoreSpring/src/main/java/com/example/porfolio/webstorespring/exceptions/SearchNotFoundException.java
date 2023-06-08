package com.example.porfolio.webstorespring.exceptions;

public class SearchNotFoundException extends RuntimeException{

    public SearchNotFoundException() {
        super("Not found resource");
    }
}
