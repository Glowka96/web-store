package com.example.portfolio.webstorespring.exceptions;

public class UnsupportedNotificationTypeException extends RuntimeException {
    public UnsupportedNotificationTypeException(String notificationTypeName) {
        super(String.format("No strategy found for notification type: %s. Please try again later.", notificationTypeName));
    }
}
