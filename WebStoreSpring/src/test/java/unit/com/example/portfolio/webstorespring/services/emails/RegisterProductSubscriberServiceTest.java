package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.ProductConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.BASIC_PRODUCT_SUBSCRIBER;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.ENABLED;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.ProductConfTokenBuilderHelper.createProductConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.BASIC_TOKEN_DETAILS;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.ProductRemovalTokenBuilderHelper.createProductRemovalTokenWithEnabledSubscriber;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.SingleProductRemovalTokenBuilderHelper.createSingleProductRemovalTokenWithEnabledSubscriber;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterProductSubscriberServiceTest {

    @Mock
    private ProductSubscriberService productSubscriberService;
    @Mock
    private ProductSubscriptionService productSubscriptionService;
    @Mock
    private ProductRemovalTokenService productRemovalTokenService;
    @Mock
    private SingleProductRemovalTokenService singleProductRemovalTokenService;
    @Mock
    private ProductConfTokenService productConfTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @InjectMocks
    private RegisterProductSubscriberService underTest;

    private ProductSubscriber productSubscriber;

    @BeforeEach
    void init() {
        productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(with(ENABLED, Boolean.FALSE))
        );
    }

    @Test
    void shouldRegisterNewUser() {
        ProductConfToken productConfToken = createProductConfToken(productSubscriber, make(a(BASIC_TOKEN_DETAILS)));
        given(productConfTokenService.create(any(ProductSubscriber.class), any(EmailType.class))).willReturn(productConfToken);

        shouldRegister(Boolean.TRUE);

        verify(emailSenderService, times(1)).sendEmail(eq(EmailType.CONFIRM_PRODUCT_SUBSCRIPTION), eq(productSubscriber.getEmail()), anyString());
    }

    @Test
    void shouldRegisterExistUserForAnotherProduct() {
        shouldRegister(Boolean.FALSE);
    }

    private void shouldRegister(boolean isFirstRegistration) {
        ProductSubscriberRequest productSubscriberRequest = new ProductSubscriberRequest(
                new SubscriberRequest("test@test.pl"),
                1L
        );

        SingleProductRemovalToken singleProductRemovalToken = createSingleProductRemovalTokenWithEnabledSubscriber();
        ProductRemovalToken productRemovalToken = createProductRemovalTokenWithEnabledSubscriber();
        ResponseMessageDTO excepted = new ResponseMessageDTO("You have successfully subscribed to this product.");

        given(productSubscriberService.saveOrReturnExistEntity(any(SubscriberRequest.class))).willReturn(productSubscriber);
        given(productSubscriberService.isFirstRegistration(any(ProductSubscriber.class))).willReturn(isFirstRegistration);
        given(singleProductRemovalTokenService.save(any(ProductSubscriber.class), anyLong())).willReturn(singleProductRemovalToken);
        given(productRemovalTokenService.save(any(ProductSubscriber.class))).willReturn(productRemovalToken);

        ResponseMessageDTO result = underTest.register(productSubscriberRequest);

        assertNotNull(result);
        assertEquals(excepted, result);

        verify(productSubscriptionService, times(1)).add(productSubscriber, 1L);
        verify(emailSenderService, times(1)).sendEmail(eq(EmailType.WELCOME_PRODUCT_SUBSCRIPTION), eq(productSubscriber.getEmail()), anyString(), anyString());
    }
}