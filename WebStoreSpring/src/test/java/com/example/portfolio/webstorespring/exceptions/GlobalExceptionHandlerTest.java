package com.example.portfolio.webstorespring.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class GlobalExceptionHandlerTest {

    @Mock
    private ResourceNotFoundException resourceNotFoundException;
    @Mock
    private MethodArgumentNotValidException argumentNotValidException;
    @Mock
    private OrderCanNotModifiedException orderCanNotModifiedException;
    @Mock
    private EmailAlreadyConfirmedException emailAlreadyConfirmedException;
    @Mock
    private AccountCanNotModifiedException accountCanNotModifiedException;
    @Mock
    private SearchNotFoundException searchNotFoundException;
    @Mock
    private BadCredentialsException badCredentialsException;
    @Mock
    private WebRequest webRequest;
    @InjectMocks
    private GlobalExceptionHandler underTest;

    @Test
    void shouldHandleResourceNotFoundException() {
        // given
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleResourceNotFoundException(resourceNotFoundException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void shouldHandleBadCredentialsException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                badCredentialsException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleBadCredentialsException(badCredentialsException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleSearchNotFoundException() {
        // given
        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                searchNotFoundException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleResourceNotFoundException(searchNotFoundException, webRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void shouldHandleArgumentNotValidException() {
        // given
        List<String> excepted = new ArrayList<>();
        excepted.add("Error message 1");
        excepted.add("Error message 2");

        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");

        // when
        when(argumentNotValidException.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                excepted,
                webRequest.getDescription(false));

        ResponseEntity<Object> responseEntity = underTest
                .handleArgumentNotValidException(argumentNotValidException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void shouldHandleCanNotModifiedExceptionWhenOrderCanNotModifiedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                orderCanNotModifiedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleCanNotModifiedException(orderCanNotModifiedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleCanNotModifiedWhenEmailAlreadyConfirmedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                emailAlreadyConfirmedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleCanNotModifiedException(emailAlreadyConfirmedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }


    @Test
    void shouldHandleAccessDeniedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                accountCanNotModifiedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .handleAccessDeniedException(accountCanNotModifiedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }
}
