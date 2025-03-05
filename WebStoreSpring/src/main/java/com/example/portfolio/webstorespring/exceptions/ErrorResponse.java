package com.example.portfolio.webstorespring.exceptions;


import java.util.Collections;
import java.util.List;

public record ErrorResponse(
        Integer statusCode,
        List<String> errors,
        String details
) {
    public ErrorResponse(Integer statusCode,
                         String errors,
                         String details) {
        this(statusCode, Collections.singletonList(errors), details);
    }
}

