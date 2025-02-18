package com.example.portfolio.webstorespring.exceptions;

import com.example.portfolio.webstorespring.enums.EmailType;

public class UnsupportedNotificationTypeException extends RuntimeException {
    public UnsupportedNotificationTypeException(EmailType emailType) {
        super(String.format("For notification type: %s this operation is unsupported. Please try again later.", emailType.name()));
    }
}
