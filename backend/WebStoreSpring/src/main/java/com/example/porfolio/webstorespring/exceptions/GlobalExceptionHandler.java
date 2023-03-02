package com.example.porfolio.webstorespring.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception,
                                                            WebRequest webRequest) {
        ErrorResponse errorResponse = createErrorResponseNotFound(exception, webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> argumentNotValidException(MethodArgumentNotValidException exception,
                                                            WebRequest webRequest) {
        ErrorResponse errorResponse = createErrorResponseBadRequest(exception, webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderCanNotModifiedException.class)
    public ResponseEntity<Object> orderCanNotModifiedException(OrderCanNotModifiedException exception,
                                                               WebRequest webRequest) {
        ErrorResponse errorResponse = createErrorResponseBadRequest(exception, webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountCanNotModifiedException.class)
    public ResponseEntity<Object> accountCanNotModifiedException(AccountCanNotModifiedException exception,
                                                                 WebRequest webRequest) {
        ErrorResponse errorResponse = createErrorResponseForbidden(exception, webRequest);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    private ErrorResponse createErrorResponseNotFound(Exception exception, WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponseBadRequest(MethodArgumentNotValidException exception,
                                                        WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList(),
                webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponseBadRequest(Exception exception,
                                                        WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponseForbidden(Exception exception,
                                                       WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                webRequest.getDescription(false));
    }
}
