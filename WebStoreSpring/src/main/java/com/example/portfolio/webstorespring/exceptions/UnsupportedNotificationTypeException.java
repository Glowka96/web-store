package com.example.portfolio.webstorespring.exceptions;

import com.example.portfolio.webstorespring.enums.NotificationType;

public class UnsupportedNotificationTypeException extends RuntimeException {
    public UnsupportedNotificationTypeException(NotificationType notificationType) {
        super(String.format("For notification type: %s this operation is unsupported. Please try again later.", notificationType.name()));
    }
}
