package com.example.portfolio.webstorespring.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, AccountHasNoAddressException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException exception,
                                                                         WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.NOT_FOUND, exception, webRequest), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                               WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(exception, webRequest), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(RuntimeException exception, WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.UNAUTHORIZED, exception, webRequest), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.FORBIDDEN, exception, webRequest), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            EmailAlreadyConfirmedException.class,
            TokenConfirmedException.class,
            TokenExpiredException.class,
            PromotionPriceGreaterThanBasePriceException.class,
            ProductHasAlreadyPromotionException.class,
            ProductsNotFoundException.class,
            ShipmentQuantityExceedsProductQuantityException.class,
    })
    public ResponseEntity<ErrorResponse> handleCanNotModifiedException(RuntimeException exception,
                                                                       WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.BAD_REQUEST, exception, webRequest), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                        WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(exception, webRequest), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handeConstraintViolationException(ConstraintViolationException exception,
                                                                           WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(exception, webRequest), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedNotificationTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedNotificationTypeException(UnsupportedNotificationTypeException exception,
                                                                                    WebRequest webRequest) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, webRequest), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(HttpStatus status, Exception exception, WebRequest webRequest) {
        return new ErrorResponse(status.value(), exception.getMessage(), webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponse(IllegalArgumentException exception, WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponse(ConstraintViolationException exception, WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .toList(),
                webRequest.getDescription(false));
    }

    private ErrorResponse createErrorResponse(MethodArgumentNotValidException exception,
                                              WebRequest webRequest) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList(),
                webRequest.getDescription(false));
    }
}
