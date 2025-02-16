package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.Subscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.RemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.NewsletterRemovalTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.ProductRemovalTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.RemovalTokenRepository;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractRemovalTokenServiceTest {

    private static final String TOKEN_VALUE = "token123";

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void shouldGetByToken(RemovalToken removalToken,
                          RemovalTokenRepository<RemovalToken> repository,
                          AbstractRemovalTokenService<RemovalToken, Subscriber> underTest) {
        given(repository.findByToken(anyString())).willReturn(Optional.of(removalToken));

        RemovalToken result = underTest.getByToken(TOKEN_VALUE);

        assertNotNull(result);
        verify(repository, times(1)).findByToken(anyString());
    }

    @ParameterizedTest
    @MethodSource("serviceProviderForSave")
    void shouldSave(RemovalToken removalToken,
                    Subscriber subscriber,
                    RemovalTokenRepository<RemovalToken> repository,
                    AbstractRemovalTokenService<RemovalToken, Subscriber> underTest) {
        given(repository.save(any(RemovalToken.class))).willReturn(removalToken);

        RemovalToken result = underTest.save(subscriber);

        assertNotNull(result);
        verify(repository, times(1)).save(any(RemovalToken.class));
    }

    @ParameterizedTest
    @MethodSource("serviceProvider")
    void shouldDeleteByToken(RemovalToken removalToken,
                             RemovalTokenRepository<RemovalToken> repository,
                             AbstractRemovalTokenService<RemovalToken, Subscriber> underTest) {
        underTest.deleteByToken(TOKEN_VALUE);
        verify(repository, times(1)).deleteByToken(TOKEN_VALUE);
    }

    private Stream<Arguments> serviceProvider() {
        NewsletterRemovalToken newsletterRemovalToken = mock(NewsletterRemovalToken.class);
        NewsletterRemovalTokenRepository newsletterRemovalTokenRepository = mock(NewsletterRemovalTokenRepository.class);
        NewsletterRemovalTokenService underTest = new NewsletterRemovalTokenService(newsletterRemovalTokenRepository);

        ProductRemovalToken productRemovalToken = mock(ProductRemovalToken.class);
        ProductRemovalTokenRepository productRemovalTokenRepository = mock(ProductRemovalTokenRepository.class);
        ProductRemovalTokenService underTest2 = new ProductRemovalTokenService(productRemovalTokenRepository);
        return Stream.of(
                Arguments.of(newsletterRemovalToken, newsletterRemovalTokenRepository, underTest),
                Arguments.of(productRemovalToken, productRemovalTokenRepository, underTest2)
        );
    }

    private Stream<Arguments> serviceProviderForSave() {
        NewsletterRemovalToken newsletterRemovalToken = mock(NewsletterRemovalToken.class);
        NewsletterSubscriber newsletterSubscriber = mock(NewsletterSubscriber.class);
        NewsletterRemovalTokenRepository newsletterRemovalTokenRepository = mock(NewsletterRemovalTokenRepository.class);
        NewsletterRemovalTokenService underTest = new NewsletterRemovalTokenService(newsletterRemovalTokenRepository);

        ProductRemovalToken productRemovalToken = mock(ProductRemovalToken.class);
        ProductSubscriber productSubscriber = mock(ProductSubscriber.class);
        ProductRemovalTokenRepository productRemovalTokenRepository = mock(ProductRemovalTokenRepository.class);
        ProductRemovalTokenService underTest2 = new ProductRemovalTokenService(productRemovalTokenRepository);
        return Stream.of(
                Arguments.of(newsletterRemovalToken, newsletterSubscriber, newsletterRemovalTokenRepository, underTest),
                Arguments.of(productRemovalToken, productSubscriber, productRemovalTokenRepository, underTest2)
        );
    }
}