package com.example.portfolio.webstorespring.exceptions;

import com.example.portfolio.webstorespring.enums.EmailType;

public class UnsupportedEmailTypeException extends RuntimeException {
    public UnsupportedEmailTypeException(EmailType emailType) {
        super(String.format("For email type: %s this operation is unsupported. Please try again later.", emailType.name()));
    }
}
