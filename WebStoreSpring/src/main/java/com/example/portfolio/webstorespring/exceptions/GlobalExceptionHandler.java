package com.example.portfolio.webstorespring.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, AccountHasNoAddressException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(RuntimeException exception,
                                                                         WebRequest webRequest) {
        return createErrorResponse(HttpStatus.NOT_FOUND, exception, webRequest);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                               WebRequest webRequest) {
        return createErrorResponse(exception, webRequest);
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, UsernameNotFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(RuntimeException exception, WebRequest webRequest) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, exception, webRequest);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(Exception exception, WebRequest webRequest) {
        return createErrorResponse(HttpStatus.FORBIDDEN, exception, webRequest);
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
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCanNotModifiedException(RuntimeException exception,
                                                                       WebRequest webRequest) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, exception, webRequest);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException exception,
                                                                        WebRequest webRequest) {
        return createErrorResponse(exception, webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeConstraintViolationException(ConstraintViolationException exception,
                                                                           WebRequest webRequest) {
        return createErrorResponse(exception, webRequest);
    }

    @ExceptionHandler(UnsupportedNotificationTypeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnsupportedNotificationTypeException(UnsupportedNotificationTypeException exception,
                                                                                    WebRequest webRequest) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, webRequest);
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
