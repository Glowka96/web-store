package com.example.portfolio.webstorespring.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private EmailAlreadyUsedException emailAlreadyUsedException;
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
        ErrorResponse resultErrorResponse = underTest.handleResourceNotFoundException(
                resourceNotFoundException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.NOT_FOUND, resourceNotFoundException
        ));
    }

    @Test
    void shouldHandleAccountHasNoAddressException_thenStatusNotFound() {
        ErrorResponse resultErrorResponse = underTest.handleResourceNotFoundException(
                accountHasNoAddressException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.NOT_FOUND, accountHasNoAddressException
        ));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException_thenStatusBadRequest() {
        List<String> excepted = new ArrayList<>();
        excepted.add("Error message 1");
        excepted.add("Error message 2");

        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");

        when(argumentNotValidException.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                excepted,
                webRequest.getDescription(false)
        );

        ErrorResponse resultErrorResponse = underTest.handleMethodArgumentNotValidException(
                argumentNotValidException, webRequest
        );

        assertsResponse(resultErrorResponse, expectedErrorResponse);
    }

    @Test
    void shouldHandleBadCredentialsException_thenStatusUnauthorized() {
        ErrorResponse resultErrorResponse = underTest.handleBadCredentialsException(
                badCredentialsException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, badCredentialsException
        ));
    }

    @Test
    void shouldHandleBadCredentialsException_whenDisableException_thenStatusUnauthorized() {
        ErrorResponse resultErrorResponse = underTest.handleBadCredentialsException(
                disabledException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, disabledException
        ));
    }

    @Test
    void shouldHandleBadCredentialsException_whenUsernameNotFoundException_thenStatusUnauthorized() {
        ErrorResponse resultErrorResponse = underTest.handleBadCredentialsException(
                usernameNotFoundException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.UNAUTHORIZED, usernameNotFoundException
        ));
    }

    @Test
    void shouldHandleAccessDeniedException_thenStatusForbidden() {
        ErrorResponse responseEntity = underTest.handleAccessDeniedException(
                accessDeniedException, webRequest
        );

        assertsResponse(responseEntity, getExceptedErrorResponse(
                HttpStatus.FORBIDDEN, accessDeniedException
        ));
    }


    @Test
    void shouldHandleCanNotModified_whenEmailAlreadyConfirmedException_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                emailAlreadyConfirmedException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, emailAlreadyConfirmedException
        ));
    }

    @Test
    void shouldHandleCanNotModified_whenTokenIsConfirmedException_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                tokenConfirmedException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, tokenConfirmedException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenTokenIsExpiredException_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                tokenExpiredException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, tokenExpiredException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenPromotionPriceGreaterThanBasicPrice_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                promotionPriceGreaterThanBasePriceException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, promotionPriceGreaterThanBasePriceException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenProductHasAlreadyPromotion_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                productHasAlreadyPromotionException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, productHasAlreadyPromotionException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenProductsNotFound_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                productsNotFoundException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, productsNotFoundException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenShipmentQualityExceedsProductQuantity_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                shipmentQuantityExceedsProductQuantityException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, shipmentQuantityExceedsProductQuantityException
        ));
    }

    @Test
    void shouldHandleCanNotModification_whenEmailAlreadyUsed_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleCanNotModifiedException(
                emailAlreadyUsedException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, emailAlreadyUsedException
        ));
    }

    @Test
    void shouldHandleIllegalArgumentException_thenStatusBadRequest() {
        ErrorResponse resultErrorResponse = underTest.handleIllegalArgumentException(
                illegalArgumentException, webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                HttpStatus.BAD_REQUEST, illegalArgumentException
        ));
    }

    @Test
    void shouldHandleConstraintViolationException() {
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

        when(mockedViolation.getMessage()).thenReturn(messages.get(0));
        when(mockedViolation2.getMessage()).thenReturn(messages.get(1));
        when(constraintViolationException.getConstraintViolations()).thenReturn(constraintViolations);

        ErrorResponse resultErrorResponse = underTest
                .handeConstraintViolationException(constraintViolationException, webRequest);

        assertEquals(exceptedErrorResponse.statusCode(), resultErrorResponse.statusCode());
        assertTrue(Objects.requireNonNull(resultErrorResponse).errors().containsAll(exceptedErrorResponse.errors()));
    }

    @Test
    void shouldHandleUnsupportedNotificationTypeException() {
        ErrorResponse resultErrorResponse = underTest.handleUnsupportedNotificationTypeException(
                unsupportedNotificationTypeException,
                webRequest
        );

        assertsResponse(resultErrorResponse, getExceptedErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        unsupportedNotificationTypeException
                )
        );
    }

    private void assertsResponse(ErrorResponse resultErrorResponse, ErrorResponse exceptedErrorResponse) {
        assertEquals(exceptedErrorResponse.statusCode(), resultErrorResponse.statusCode());
        assertEquals(exceptedErrorResponse, resultErrorResponse);
    }

    private <E extends RuntimeException> ErrorResponse getExceptedErrorResponse(HttpStatus httpStatus, E exception) {
        return new ErrorResponse(
                httpStatus.value(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );
    }
}
