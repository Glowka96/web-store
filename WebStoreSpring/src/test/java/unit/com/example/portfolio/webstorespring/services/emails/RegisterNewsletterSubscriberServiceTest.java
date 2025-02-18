package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.NewsletterConfTokenBuilderHelper;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.NewsletterConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createDisabledNewsletterSubscriber;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.BASIC_TOKEN_DETAILS;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterNewsletterSubscriberServiceTest {

    @Mock
    private NewsletterSubscriberService newsletterSubscriberService;
    @Mock
    private NewsletterRemovalTokenService newsletterRemovalTokenService;
    @Mock
    private NewsletterConfTokenService newsletterConfTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @InjectMocks
    private RegisterNewsletterSubscriberService underTest;

    @Test
    void shouldRegister() {
        SubscriberRequest subscriberRequest = new SubscriberRequest("test@test.pl");
        NewsletterSubscriber newsletterSubscriber = createDisabledNewsletterSubscriber();
        NewsletterConfToken newsletterConfToken = NewsletterConfTokenBuilderHelper.createNewsletterConfToken(newsletterSubscriber, make(a(BASIC_TOKEN_DETAILS)));
        ResponseMessageDTO excepted = new ResponseMessageDTO("Verify your email address using the link in your email.");

        given(newsletterSubscriberService.save(any(SubscriberRequest.class))).willReturn(newsletterSubscriber);
        given(newsletterConfTokenService.create(any(NewsletterSubscriber.class), any(EmailType.class))).willReturn(newsletterConfToken);

        ResponseMessageDTO result = underTest.register(subscriberRequest);

        assertNotNull(result);
        assertEquals(excepted, result);
        verify(emailSenderService, times(1)).sendEmail(eq(EmailType.CONFIRM_NEWSLETTER), eq(newsletterSubscriber.getEmail()), anyString());
    }
}