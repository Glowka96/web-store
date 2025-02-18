package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.Subscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.RemovalToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.NewsletterConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.ProductConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.AbstractRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractConfirmEmailServiceTest {

    private static final String EMAIL = "test@test.pl";
    private static final String TOKEN = "token123";

    @ParameterizedTest
    @MethodSource("serviceProviderForConfirmationEmailForOthers")
    void shouldConfirmTokenOrResend(ConfToken confToken,
                                    OwnerConfToken ownerConfToken,
                                    AbstractConfTokenService<ConfToken, OwnerConfToken> confTokenService,
                                    EmailType emailType,
                                    AbstractConfirmEmailService<ConfToken, OwnerConfToken, AbstractConfTokenService<ConfToken, OwnerConfToken>> underTest) {
        setupTest(confToken, ownerConfToken, confTokenService);

        ResponseMessageDTO result = underTest.confirmTokenOrResend(TOKEN, emailType);

        assertsTest(ownerConfToken, result);
    }

    @ParameterizedTest
    @MethodSource("serviceProviderForConfirmationEmailForNewsletter")
    void shouldConfirmTokenOrResendNewsletter(ConfToken confToken,
                                              OwnerConfToken ownerConfToken,
                                              RemovalToken removalToken,
                                              AbstractRemovalTokenService<RemovalToken, Subscriber> removalTokenService,
                                              AbstractConfTokenService<ConfToken, OwnerConfToken> confTokenService,
                                              EmailType reconfirmEmailType,
                                              AbstractConfirmEmailService<ConfToken, OwnerConfToken, AbstractConfTokenService<ConfToken, OwnerConfToken>> underTest) {
        setupTest(confToken, ownerConfToken, confTokenService);

        given(removalTokenService.save(any(Subscriber.class))).willReturn(removalToken);
        given(ownerConfToken.getEmail()).willReturn(EMAIL);
        given(removalToken.getToken()).willReturn("removalToken123");

        ResponseMessageDTO result = underTest.confirmTokenOrResend(TOKEN, reconfirmEmailType);

        assertsTest(ownerConfToken, result);
    }

    @ParameterizedTest
    @MethodSource("serviceProviderForResendConfirmationEmail")
    void shouldResendConfirmationEmail_whenOwnerDisabledAndTokenExpired(ConfToken confToken,
                                                                        OwnerConfToken ownerConfToken,
                                                                        AbstractConfTokenService<ConfToken, OwnerConfToken> confTokenService,
                                                                        EmailType emailType,
                                                                        AbstractConfirmEmailService<ConfToken, OwnerConfToken, AbstractConfTokenService<ConfToken, OwnerConfToken>> underTest) {
        given(confTokenService.getByToken(anyString())).willReturn(confToken);
        given(confTokenService.extractRelatedEntity(any(ConfToken.class))).willReturn(ownerConfToken);
        doNothing().when(confTokenService).validateTokenConfirmedOrOwnerEnabled(any(ConfToken.class), any(OwnerConfToken.class));
        given(confTokenService.isOwnerDisabledAndTokenExpired(ownerConfToken, confToken)).willReturn(Boolean.TRUE);
        given(confTokenService.create(ownerConfToken, emailType)).willReturn(confToken);
        given(ownerConfToken.getEmail()).willReturn(EMAIL);
        given(confToken.getToken()).willReturn("newToken123");

        ResponseMessageDTO result = underTest.confirmTokenOrResend(TOKEN, emailType);

        assertNotNull(result);
        verify(confTokenService, times(1)).delete(confToken);
        assertEquals("Your token is expired. There is new confirmation link in your email.", result.message());
    }

    private static void setupTest(ConfToken confToken, OwnerConfToken ownerConfToken, AbstractConfTokenService<ConfToken, OwnerConfToken> confTokenService) {
        doCallRealMethod().when(ownerConfToken).getName();
        given(confTokenService.getByToken(anyString())).willReturn(confToken);
        given(confTokenService.extractRelatedEntity(any(ConfToken.class))).willReturn(ownerConfToken);

        doNothing().when(confTokenService).validateTokenConfirmedOrOwnerEnabled(any(ConfToken.class), any(OwnerConfToken.class));
        given(confTokenService.isOwnerDisabledAndTokenExpired(ownerConfToken, confToken)).willReturn(Boolean.FALSE);
        doNothing().when(confTokenService).setConfirmedAt(confToken);
    }

    private static void assertsTest(OwnerConfToken ownerConfToken, ResponseMessageDTO result) {
        assertNotNull(result);
        assertEquals(String.format("%s confirmed.", ownerConfToken.getName()), result.message());
        verify(ownerConfToken, times(1)).setEnabled(Boolean.TRUE);
    }

    private Stream<Arguments> serviceProviderForConfirmationEmailForOthers() {
        EmailSenderService emailSenderService = mock(EmailSenderService.class);
        ProductConfToken productConfToken = mock(ProductConfToken.class);
        ProductSubscriber productSubscriber = mock(ProductSubscriber.class);
        ProductSubscriberService productSubscriberService = mock(ProductSubscriberService.class);
        ProductSubscriptionService productSubscriptionService = mock(ProductSubscriptionService.class);
        ProductConfTokenService productConfTokenService = mock(ProductConfTokenService.class);
        ProductRemovalTokenService productRemovalTokenService = mock(ProductRemovalTokenService.class);
        SingleProductRemovalTokenService singleProductRemovalTokenService = mock(SingleProductRemovalTokenService.class);
        RegisterProductSubscriberService underTest2 = new RegisterProductSubscriberService(emailSenderService, productConfTokenService, productSubscriptionService, productSubscriberService, productRemovalTokenService, singleProductRemovalTokenService);

        AccountConfToken accountConfToken = mock(AccountConfToken.class);
        Account account = mock(Account.class);
        AccountService accountService = mock(AccountService.class);
        AccountConfTokenService accountConfTokenService = mock(AccountConfTokenService.class);
        RegistrationService underTest3 = new RegistrationService(emailSenderService, accountConfTokenService, accountService);
        return Stream.of(
                Arguments.of(productConfToken, productSubscriber, productConfTokenService, EmailType.RECONFIRM_PRODUCT_SUBSCRIPTION, underTest2),
                Arguments.of(accountConfToken, account, accountConfTokenService, EmailType.RECONFIRM_EMAIL, underTest3)
        );
    }

    private Stream<Arguments> serviceProviderForConfirmationEmailForNewsletter() {
        EmailSenderService emailSenderService = mock(EmailSenderService.class);
        NewsletterConfToken newsletterConfToken = mock(NewsletterConfToken.class);
        NewsletterSubscriber newsletterSubscriber = mock(NewsletterSubscriber.class);
        NewsletterRemovalToken newsletterRemovalToken = mock(NewsletterRemovalToken.class);
        NewsletterSubscriberService newsletterSubscriberService = mock(NewsletterSubscriberService.class);
        NewsletterConfTokenService newsletterConfTokenService = mock(NewsletterConfTokenService.class);
        NewsletterRemovalTokenService newsletterRemovalTokenService = mock(NewsletterRemovalTokenService.class);
        RegisterNewsletterSubscriberService underTest1 = new RegisterNewsletterSubscriberService(emailSenderService, newsletterConfTokenService, newsletterSubscriberService, newsletterRemovalTokenService);
        return Stream.of(
                Arguments.of(newsletterConfToken, newsletterSubscriber, newsletterRemovalToken, newsletterRemovalTokenService, newsletterConfTokenService, EmailType.RECONFIRM_NEWSLETTER, underTest1)
        );
    }

    private Stream<Arguments> serviceProviderForResendConfirmationEmail() {
        return Stream.concat(
                serviceProviderForConfirmationEmailForOthers()
                        .map(args -> Arguments.of(
                                args.get()[0],
                                args.get()[1],
                                args.get()[2],
                                args.get()[3],
                                args.get()[4]
                        )),
                serviceProviderForConfirmationEmailForNewsletter()
                        .map(args -> Arguments.of(
                                args.get()[0],
                                args.get()[1],
                                args.get()[4],
                                args.get()[5],
                                args.get()[6]
                        ))
        );
    }
}