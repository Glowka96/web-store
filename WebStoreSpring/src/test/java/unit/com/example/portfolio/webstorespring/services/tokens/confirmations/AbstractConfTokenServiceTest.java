package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.*;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.NewsletterConfTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ProductConfTokenRepository;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.BASIC_TOKEN_DETAILS;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.CONFIRMED_AT;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractConfTokenServiceTest {

    private TokenDetailsService tokenDetailsService;
    private NotificationExpirationManager notificationExpirationManager;

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void getByToken(ConfToken confToken,
                    OwnerConfToken ownerConfToken,
                    ConfirmationTokenRepository<ConfToken> repository,
                    AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {
        given(repository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(confToken));

        ConfToken result = underTest.getByToken("token123");

        assertNotNull(result);
        assertEquals(confToken, result);
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void willThrowResourceNotFoundException_whenConfTokenNotFound(ConfToken confToken,
                                                                  OwnerConfToken ownerConfToken,
                                                                  ConfirmationTokenRepository<ConfToken> repository,
                                                                  AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {
        given(repository.findByTokenDetails_Token(anyString())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> underTest.getByToken("token123"));
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void shouldCreate(ConfToken confToken,
                      OwnerConfToken ownerConfToken,
                      ConfirmationTokenRepository<ConfToken> repository,
                      AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS));

        given(ownerConfToken.getName()).willCallRealMethod();
        given(notificationExpirationManager.getExpirationMinutes(any(NotificationType.class))).willReturn(15L);
        given(tokenDetailsService.create(anyLong())).willReturn(tokenDetails);
        given(repository.save(any(ConfToken.class))).willReturn(confToken);

        ConfToken result = underTest.create(ownerConfToken, NotificationType.CONFIRM_EMAIL);

        assertNotNull(result);
        assertEquals(confToken, result);
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void delete(ConfToken confToken,
                OwnerConfToken ownerConfToken,
                ConfirmationTokenRepository<ConfToken> repository,
                AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {
        underTest.delete(confToken);

        verify(repository, times(1)).delete(confToken);
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void willThrowEmailAlreadyConfirmedException_whenTokenIsConfirmedOrOwnerIsEnabled(ConfToken confToken,
                                                                                      OwnerConfToken ownerConfToken,
                                                                                      ConfirmationTokenRepository<ConfToken> repository,
                                                                                      AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {

        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS));
        given(confToken.getTokenDetails()).willReturn(tokenDetails);
        given(ownerConfToken.getEnabled()).willReturn(Boolean.TRUE);

        assertThrows(EmailAlreadyConfirmedException.class, () -> underTest.validateTokenConfirmedOrOwnerEnabled(confToken, ownerConfToken));
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void willNotThrowEmailAlreadyConfirmedException_whenTokenIsNotConfirmedOrOwnerIsNotEnabled(ConfToken confToken,
                                                                                               OwnerConfToken ownerConfToken,
                                                                                               ConfirmationTokenRepository<ConfToken> repository,
                                                                                               AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {

        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(CONFIRMED_AT)));
        given(confToken.getTokenDetails()).willReturn(tokenDetails);
        given(ownerConfToken.getEnabled()).willReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> underTest.validateTokenConfirmedOrOwnerEnabled(confToken, ownerConfToken));
    }

    @ParameterizedTest
    @MethodSource("serviceAndEnabledProvider")
    void shouldValidateIsOwnerDisabledAndTokenExpired(ConfToken confToken,
                                                      OwnerConfToken ownerConfToken,
                                                      AbstractConfTokenService<ConfToken, OwnerConfToken> underTest,
                                                      Boolean ownerEnabled,
                                                      Boolean isTokenExpired,
                                                      Boolean exceptResult) {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(CONFIRMED_AT)));
        given(confToken.getTokenDetails()).willReturn(tokenDetails);
        given(ownerConfToken.getEnabled()).willReturn(ownerEnabled);
        given(tokenDetailsService.isTokenExpired(confToken.getTokenDetails())).willReturn(isTokenExpired);

        Boolean result = underTest.isOwnerDisabledAndTokenExpired(ownerConfToken, confToken);

        assertEquals(exceptResult, result);
    }

    @ParameterizedTest
    @MethodSource("serviceProviderForSetConfirmed")
    void setConfirmedAt(ConfToken confToken,
                        AbstractConfTokenService<ConfToken, OwnerConfToken> underTest) {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS).but(withNull(CONFIRMED_AT)));

        Clock clock = mock(Clock.class);
        tokenDetailsService = spy(new TokenDetailsService(clock));
        ReflectionTestUtils.setField(underTest, "tokenDetailsService", tokenDetailsService);

        given(confToken.getTokenDetails()).willReturn(tokenDetails);
        given(clock.getZone()).willReturn(ZONED_DATE_TIME.getZone());
        given(clock.instant()).willReturn(ZONED_DATE_TIME.toInstant());

        underTest.setConfirmedAt(confToken);

        assertNotNull(tokenDetails.getConfirmedAt());
    }

    private Stream<Arguments> serviceProvider() {
        tokenDetailsService = mock(TokenDetailsService.class);
        notificationExpirationManager = mock(NotificationExpirationManager.class);

        ProductConfToken productConfToken = mock(ProductConfToken.class);
        ProductSubscriber productSubscriber = mock(ProductSubscriber.class);
        ProductConfTokenRepository productConfTokenRepository = mock(ProductConfTokenRepository.class);
        ProductConfTokenService underTest1 = new ProductConfTokenService(productConfTokenRepository, tokenDetailsService, notificationExpirationManager);

        NewsletterConfToken newsletterConfToken = mock(NewsletterConfToken.class);
        NewsletterSubscriber newsletterSubscriber = mock(NewsletterSubscriber.class);
        NewsletterConfTokenRepository newsletterConfTokenRepository = mock(NewsletterConfTokenRepository.class);
        NewsletterConfTokenService underTest2 = new NewsletterConfTokenService(newsletterConfTokenRepository, tokenDetailsService, notificationExpirationManager);

        AccountConfToken accountConfToken = mock(AccountConfToken.class);
        Account account = mock(Account.class);
        AccountConfTokenRepository accountConfTokenRepository = mock(AccountConfTokenRepository.class);
        AccountConfTokenService underTest3 = new AccountConfTokenService(accountConfTokenRepository, tokenDetailsService, notificationExpirationManager);
        return Stream.of(
                Arguments.of(productConfToken, productSubscriber, productConfTokenRepository, underTest1),
                Arguments.of(newsletterConfToken, newsletterSubscriber, newsletterConfTokenRepository, underTest2),
                Arguments.of(accountConfToken, account, accountConfTokenRepository, underTest3)
        );
    }

    private Stream<Arguments> serviceProviderForSetConfirmed() {
        notificationExpirationManager = mock(NotificationExpirationManager.class);

        ProductConfToken productConfToken = mock(ProductConfToken.class);
        ProductConfTokenRepository productConfTokenRepository = mock(ProductConfTokenRepository.class);
        ProductConfTokenService underTest1 = new ProductConfTokenService(productConfTokenRepository, tokenDetailsService, notificationExpirationManager);

        NewsletterConfToken newsletterConfToken = mock(NewsletterConfToken.class);
        NewsletterConfTokenRepository newsletterConfTokenRepository = mock(NewsletterConfTokenRepository.class);
        NewsletterConfTokenService underTest2 = new NewsletterConfTokenService(newsletterConfTokenRepository, tokenDetailsService, notificationExpirationManager);

        AccountConfToken accountConfToken = mock(AccountConfToken.class);
        AccountConfTokenRepository accountConfTokenRepository = mock(AccountConfTokenRepository.class);
        AccountConfTokenService underTest3 = new AccountConfTokenService(accountConfTokenRepository, tokenDetailsService, notificationExpirationManager);
        return Stream.of(
                Arguments.of(productConfToken, underTest1),
                Arguments.of(newsletterConfToken, underTest2),
                Arguments.of(accountConfToken, underTest3)
        );
    }

    private Stream<Arguments> serviceAndEnabledProvider() {
        return serviceProvider().flatMap(args -> {
            ConfToken confToken = (ConfToken) args.get()[0];
            OwnerConfToken ownerConfToken = (OwnerConfToken) args.get()[1];
            AbstractConfTokenService<ConfToken, OwnerConfToken> underTest = (AbstractConfTokenService<ConfToken, OwnerConfToken>) args.get()[3];

            return Stream.of(
                    Arguments.of(confToken, ownerConfToken, underTest, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE),
                    Arguments.of(confToken, ownerConfToken, underTest, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE),
                    Arguments.of(confToken, ownerConfToken, underTest, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE),
                    Arguments.of(confToken, ownerConfToken, underTest, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)
            );
        });
    }
}