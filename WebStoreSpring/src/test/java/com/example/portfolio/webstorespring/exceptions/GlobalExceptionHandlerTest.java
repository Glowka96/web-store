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
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private AccountHasNoAddressException accountHasNoAddressException;
    @Mock
    private MethodArgumentNotValidException argumentNotValidException;
    @Mock
    private BadCredentialsException badCredentialsException;
    @Mock
    private DisabledException disabledException;
    @Mock
    private UsernameNotFoundException usernameNotFoundException;
    @Mock
    private AccessDeniedException accessDeniedException;
    @Mock
    private EmailAlreadyConfirmedException emailAlreadyConfirmedException;
    @Mock
    private TokenConfirmedException tokenConfirmedException;
    @Mock
    private TokenExpiredException tokenExpiredException;
    @Mock
    private PromotionPriceGreaterThanBasePriceException promotionPriceGreaterThanBasePriceException;
    @Mock
    private ProductHasAlreadyPromotionException productHasAlreadyPromotionException;
    @Mock
    private ProductsNotFoundException productsNotFoundException;
    @Mock
    private ShipmentQuantityExceedsProductQuantityException shipmentQuantityExceedsProductQuantityException;
    @Mock
    private IllegalArgumentException illegalArgumentException;
    @Mock
    private ConstraintViolationException constraintViolationException;
    @Mock
    private UnsupportedNotificationTypeException unsupportedNotificationTypeException;
    @Mock
    private WebRequest webRequest;
    @InjectMocks
    private GlobalExceptionHandler underTest;

    @Test
    void shouldHandleResourceNotFoundException_thenStatusNotFound() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleResourceNotFoundException(
                resourceNotFoundException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.NOT_FOUND, resourceNotFoundException
        ));
    }

    @Test
    void shouldHandleAccountHasNoAddressException_thenStatusNotFound() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleResourceNotFoundException(
                accountHasNoAddressException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.NOT_FOUND, accountHasNoAddressException
        ));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException_thenStatusBadRequest() {
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
                webRequest.getDescription(false)
        );

        ResponseEntity<ErrorResponse> responseEntity = underTest.handleMethodArgumentNotValidException(
                argumentNotValidException, webRequest
        );

        // then
        assertsResponse(responseEntity, expectedErrorResponse);
    }

    @Test
    void shouldHandleBadCredentialsException_thenStatusUnauthorized() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleBadCredentialsException(
                badCredentialsException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, badCredentialsException
        ));
    }

    @Test
    void shouldHandleBadCredentialsException_whenDisableException_thenStatusUnauthorized() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleBadCredentialsException(
                disabledException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, disabledException
        ));
    }

    @Test
    void shouldHandleBadCredentialsException_whenUsernameNotFoundException_thenStatusUnauthorized() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleBadCredentialsException(
                usernameNotFoundException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, usernameNotFoundException
        ));
    }

    @Test
    void shouldHandleAccessDeniedException_thenStatusForbidden() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleAccessDeniedException(
                accessDeniedException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.FORBIDDEN, accessDeniedException
        ));
    }


    @Test
    void shouldHandleCanNotModified_whenEmailAlreadyConfirmedException_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                emailAlreadyConfirmedException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, emailAlreadyConfirmedException
        ));
    }

    @Test
    void shouldHandleCanNotModified_whenTokenIsConfirmedException_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                tokenConfirmedException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, tokenConfirmedException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenTokenIsExpiredException_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                tokenExpiredException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, tokenExpiredException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenPromotionPriceGreaterThanBasicPrice_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                promotionPriceGreaterThanBasePriceException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, promotionPriceGreaterThanBasePriceException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenProductHasAlreadyPromotion_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                productHasAlreadyPromotionException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, productHasAlreadyPromotionException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenProductsNotFound_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                productsNotFoundException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, productsNotFoundException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenShipmentQualityExceedsProductQuantity_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleCanNotModifiedException(
                shipmentQuantityExceedsProductQuantityException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, shipmentQuantityExceedsProductQuantityException
        ));
    }

    @Test
    void shouldHandleIllegalArgumentException_thenStatusBadRequest() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleIllegalArgumentException(
                illegalArgumentException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, illegalArgumentException
        ));
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
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getErrors()).containsExactlyInAnyOrderElementsOf(exceptedErrorResponse.getErrors());

    }

    @Test
    void shouldHandleUnsupportedNotificationTypeException() {
        ResponseEntity<ErrorResponse> responseEntity = underTest.handleUnsupportedNotificationTypeException(
                unsupportedNotificationTypeException,
                webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        unsupportedNotificationTypeException
                )
        );
    }

    private void assertsResponse(ResponseEntity<ErrorResponse> responseEntity, ErrorResponse exceptedErrorResponse) {
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(exceptedErrorResponse.getStatusCode());
        assertThat(responseEntity.getBody()).isEqualTo(exceptedErrorResponse);
    }

    private <E extends RuntimeException> ErrorResponse getExceptedErrorResponse(HttpStatus httpStatus, E exception) {
        return new ErrorResponse(
                httpStatus.value(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );
    }
}
