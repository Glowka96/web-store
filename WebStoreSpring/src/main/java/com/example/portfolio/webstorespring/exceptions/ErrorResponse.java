package com.example.portfolio.webstorespring.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public final class ErrorResponse {
    private final Integer statusCode;
    private final List<String> errors;
    private final String details;

    public ErrorResponse(Integer statusCode,
                         String errors,
                         String details) {
        this.statusCode = statusCode;
        this.errors = Collections.singletonList(errors);
        this.details = details;
    }

    public ErrorResponse(Integer statusCode,
                         List<String> errors,
                         String details) {
        this.statusCode = statusCode;
        this.errors = errors;
        this.details = details;
    }
}

