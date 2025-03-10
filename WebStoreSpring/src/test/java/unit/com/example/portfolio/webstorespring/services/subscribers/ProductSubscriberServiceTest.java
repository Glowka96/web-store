package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriptionBuilderHelper.createProductSubscription;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSubscriberServiceTest {

    @Mock
    private ProductSubscriberRepository productSubscriberRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private ProductSubscriberService underTest;

    @Test
    void shouldFindWithSubscriptionById() {
        ProductSubscription productSubscription = createProductSubscription();
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(with(SUBSCRIPTIONS, Set.of(productSubscription))));

        given(productSubscriberRepository.findWithSubscriptionById(anyLong())).willReturn(Optional.of(productSubscriber));

        ProductSubscriber result = underTest.findWithSubscriptionById(1L);

        assertEquals(productSubscriber, result);
    }

    @Test
    void willThrowResourceNotFoundException_WhenNotFoundProductSubscriber() {
        given(productSubscriberRepository.findWithSubscriptionById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> underTest.findWithSubscriptionById(anyLong()));
    }

    @Test
    void shouldSave_whenThisEmailAlreadyNotExist() {
        SubscriberRequest request = new SubscriberRequest("test@test.pl");
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));

        given(productSubscriberRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(productSubscriberRepository.save(any(ProductSubscriber.class))).willReturn(productSubscriber);

        ProductSubscriber result = underTest.saveOrReturnExistEntity(request);

        assertEquals(request.email(), result.getEmail());
        assertEquals(productSubscriber, result);
        verify(productSubscriberRepository, times(1)).save(any(ProductSubscriber.class));
    }

    @Test
    void shouldReturnExistEntity_whenSaveSubscriberWithEmailWhatAlreadyExist() {
        SubscriberRequest request = new SubscriberRequest("test@test.pl");
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));

        given(productSubscriberRepository.findByEmail(anyString())).willReturn(Optional.of(productSubscriber));

        ProductSubscriber result = underTest.saveOrReturnExistEntity(request);

        assertEquals(productSubscriber, result);
        verifyNoMoreInteractions(productSubscriberRepository);
    }

    @Test
    void shouldDelete() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));
        ResponseMessageDTO expect = new ResponseMessageDTO("Subscriber is unsubscribed.");

        ResponseMessageDTO result = underTest.delete(productSubscriber);

        assertEquals(expect, result);
    }

    @ParameterizedTest
    @MethodSource("provideFirstRegistrationTestCases")
    void shouldValidateFirstRegistration(ProductSubscriber productSubscriber, boolean expected) {
        given(clock.getZone()).willReturn(ZONED_DATE_TIME.getZone());
        given(clock.instant()).willReturn(ZONED_DATE_TIME.toInstant());

        Boolean result = underTest.isFirstRegistration(productSubscriber);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideFirstRegistrationTestCases() {
        return Stream.of(
                Arguments.of(
                        make(a(BASIC_PRODUCT_SUBSCRIBER)
                                .but(with(ENABLED, Boolean.FALSE))),
                        true
                ),
                Arguments.of(
                        make(a(BASIC_PRODUCT_SUBSCRIBER)),
                        false
                ),
                Arguments.of(
                        make(a(BASIC_PRODUCT_SUBSCRIBER)
                                .but(with(ENABLED, Boolean.FALSE))
                                .but(with(CREATED_AT, LOCAL_DATE_TIME.plusMinutes(15)))),
                        false
                )
        );
    }
}