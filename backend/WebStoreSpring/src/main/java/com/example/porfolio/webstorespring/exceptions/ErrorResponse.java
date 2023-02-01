package com.example.porfolio.webstorespring.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public record ErrorResponse(LocalDateTime timestamp,
                            Integer statusCode,
                            String message,
                            String details) {
}

