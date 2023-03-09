package com.example.porfolio.webstorespring.exceptions;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
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

