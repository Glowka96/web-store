package com.example.portfolio.webstorespring.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class GlobalExceptionHandlerTest {

    @Mock
    private ResourceNotFoundException resourceNotFoundException;
    @Mock
    private MethodArgumentNotValidException argumentNotValidException;
    @Mock
    private OrderCanNotModifiedException orderCanNotModifiedException;
    @Mock
    private EmailAlreadyConfirmedException emailAlreadyConfirmedException;
    @Mock
    private AccessDeniedException accessDeniedException;
    @Mock
    private BadCredentialsException badCredentialsException;
    @Mock
    private TokenExpiredException tokenExpiredException;
    @Mock
    private TokenConfirmedException tokenConfirmedException;
    @Mock
    private ConstraintViolationException constraintViolationException;
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
        ResponseEntity<ErrorResponse> responseEntity = underTest
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
        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleBadCredentialsException(badCredentialsException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
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

        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleMethodArgumentNotValidException(argumentNotValidException, webRequest);

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
        ResponseEntity<ErrorResponse> responseEntity = underTest
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
        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleCanNotModifiedException(emailAlreadyConfirmedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleCanNotModifiedWhenTokenIsConfirmedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                tokenConfirmedException.getMessage(),
                webRequest.getDescription(false));

        //when
        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleCanNotModifiedException(tokenConfirmedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleCanNotModificationWhenTokenIsExpiredException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                tokenExpiredException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleCanNotModifiedException(tokenExpiredException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleAccessDeniedException() {
        // given
        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                accessDeniedException.getMessage(),
                webRequest.getDescription(false));

        // when
        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handleAccessDeniedException(accessDeniedException, webRequest);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    @Test
    void shouldHandleConstraintViolationException() {
        // given
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        ConstraintViolation mockedViolation = mock(ConstraintViolation.class);
        ConstraintViolation mockedViolation2 = mock(ConstraintViolation.class);

        constraintViolations.add(mockedViolation);
        constraintViolations.add(mockedViolation2);

        List<String> messages = List.of("test", "test2");

        ErrorResponse exceptedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                messages,
                webRequest.getDescription(false)
        );

        // when
        when(mockedViolation.getMessage()).thenReturn(messages.get(0));
        when(mockedViolation2.getMessage()).thenReturn(messages.get(1));
        when(constraintViolationException.getConstraintViolations()).thenReturn(constraintViolations);

        ResponseEntity<ErrorResponse> responseEntity = underTest
                .handeConstraintViolationException(constraintViolationException, webRequest);

        // then

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody().getErrors()).containsExactlyInAnyOrderElementsOf(exceptedErrorResponse.getErrors());

    }
}
