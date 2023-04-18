package com.example.porfolio.webstorespring.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private WebRequest webRequest;
    @InjectMocks
    private GlobalExceptionHandler underTest;

    @Test
    void testResourceNotFoundException() {
        // given
        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .resourceNotFoundException(resourceNotFoundException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void testSearchNotFoundException() {
        // given
        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                searchNotFoundException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .resourceNotFoundException(searchNotFoundException, webRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void testMethodArgumentNotValidException() {
        // given
        List<String> excepted = new ArrayList<>();
        excepted.add("Error message 1");
        excepted.add("Error message 2");

        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");

        // when
        when(argumentNotValidException.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                excepted,
                webRequest.getDescription(false));

        ResponseEntity<Object> responseEntity = underTest
                .argumentNotValidException(argumentNotValidException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(expectedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(expectedErrorResponse);
    }

    @Test
    void testMethodCanNotModifiedExceptionWhenOrderCanNotModifiedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                orderCanNotModifiedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .canNotModifiedException(orderCanNotModifiedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void testMethodCanNotModifiedWhenEmailAlreadyConfirmedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                emailAlreadyConfirmedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .canNotModifiedException(emailAlreadyConfirmedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }


    @Test
    void testMethodAccessDeniedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                accountCanNotModifiedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<Object> responseEntity = underTest
                .accessDeniedException(accountCanNotModifiedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }
}