package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum AccessDeniedExceptionMessage {

    GET("You can only get your data"),
    UPDATE("You can only update your data"),
    DELETE("You can only delete your data");

    private final String message;

    AccessDeniedExceptionMessage(String message) {
        this.message = message;
    }
}
