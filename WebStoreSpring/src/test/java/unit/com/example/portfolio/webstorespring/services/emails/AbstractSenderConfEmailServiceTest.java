package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entities.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.emails.accountactions.ResetPasswordService;
import com.example.portfolio.webstorespring.services.emails.accountactions.RestoreEmailService;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterNewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterProductSubscriberService;
import com.example.portfolio.webstorespring.services.emails.registrations.RegistrationService;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AbstractConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.NewsletterConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.ProductConfTokenService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractSenderConfEmailServiceTest {

    private EmailSenderService emailSenderService;

    @ParameterizedTest
    @MethodSource("serviceProviderForConfirmationEmail")
    void shouldSendConfirmationEmail(ConfToken confToken,
                                     OwnerConfToken ownerConfToken,
                                     AbstractConfTokenService<ConfToken, OwnerConfToken> confTokenService,
                                     EmailType emailType,
                                     AbstractSenderConfEmailService<ConfToken, OwnerConfToken, AbstractConfTokenService<ConfToken, OwnerConfToken>> underTest) {
        given(ownerConfToken.getEmail()).willReturn("test@test.pl");
        given(confToken.getToken()).willReturn("token123");
        given(confTokenService.create(any(OwnerConfToken.class), any(EmailType.class))).willReturn(confToken);

        underTest.sendConfirmationEmail(ownerConfToken, emailType);

        verify(emailSenderService, times(1)).sendEmail(emailType, ownerConfToken.getEmail(), confToken.getToken());
    }

    private Stream<Arguments> serviceProviderForConfirmationEmail() {
        emailSenderService = mock(EmailSenderService.class);
        NewsletterConfToken newsletterConfToken = mock(NewsletterConfToken.class);
        NewsletterSubscriber newsletterSubscriber = mock(NewsletterSubscriber.class);
        NewsletterSubscriberService newsletterSubscriberService = mock(NewsletterSubscriberService.class);
        NewsletterConfTokenService newsletterConfTokenService = mock(NewsletterConfTokenService.class);
        NewsletterRemovalTokenService newsletterRemovalTokenService = mock(NewsletterRemovalTokenService.class);
        RegisterNewsletterSubscriberService underTest1 = new RegisterNewsletterSubscriberService(emailSenderService, newsletterConfTokenService, newsletterSubscriberService, newsletterRemovalTokenService);

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
        ResetPasswordService underTest4 = new ResetPasswordService(emailSenderService, accountConfTokenService, accountService);
        RestoreEmailService underTest5 = new RestoreEmailService(emailSenderService, accountConfTokenService, accountService);
        return Stream.of(
                Arguments.of(newsletterConfToken, newsletterSubscriber, newsletterConfTokenService, EmailType.CONFIRM_NEWSLETTER, underTest1),
                Arguments.of(productConfToken, productSubscriber, productConfTokenService, EmailType.CONFIRM_PRODUCT_SUBSCRIPTION, underTest2),
                Arguments.of(accountConfToken, account, accountConfTokenService, EmailType.CONFIRM_EMAIL, underTest3),
                Arguments.of(accountConfToken, account, accountConfTokenService, EmailType.RESET_PASSWORD, underTest4),
                Arguments.of(accountConfToken, account, accountConfTokenService, EmailType.RESTORE_EMAIL, underTest5)
        );
    }
}