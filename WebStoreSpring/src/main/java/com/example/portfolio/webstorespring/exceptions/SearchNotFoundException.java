package com.example.portfolio.webstorespring.exceptions;

public class SearchNotFoundException extends RuntimeException{

    public SearchNotFoundException() {
        super("Not found resource");
    }
}
