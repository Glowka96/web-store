package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyUsedException;
import com.example.portfolio.webstorespring.models.dtos.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterSubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createDisabledNewsletterSubscriber;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createEnabledNewsletterSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NewsletterSubscriberServiceTest {

    @Mock
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @InjectMocks
    private NewsletterSubscriberService underTest;

    @Test
    void shouldGetAllEnabled() {
        Set<NewsletterSubscriber> enabledSubscribers = Set.of(createEnabledNewsletterSubscriber(), createEnabledNewsletterSubscriber());

        given(newsletterSubscriberRepository.findAllByEnabledIsTrue()).willReturn(enabledSubscribers);

        Set<NewsletterSubscriber> result = underTest.getAllEnabled();

        assertEquals(enabledSubscribers, result);
    }

    @Test
    void shouldSave_whenThisEmailIsNotExist() {
        SubscriberRequest subscriberRequest = new SubscriberRequest("test@test.pl");
        NewsletterSubscriber excepted = createDisabledNewsletterSubscriber();

        given(newsletterSubscriberRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(newsletterSubscriberRepository.save(any(NewsletterSubscriber.class))).willReturn(excepted);

        NewsletterSubscriber result = underTest.save(subscriberRequest);

        assertEquals(excepted, result);
    }

    @Test
    void willThrowEmailAlreadyUsedException_whenThisEmailIsAlreadyExist() {
        SubscriberRequest subscriberRequest = new SubscriberRequest("test@test.pl");

        given(newsletterSubscriberRepository.existsByEmail(anyString())).willReturn(Boolean.TRUE);

        assertThrows(EmailAlreadyUsedException.class, () -> underTest.save(subscriberRequest));
    }

    @Test
    void shouldDeleteById() {
        underTest.deleteById(anyLong());

        verify(newsletterSubscriberRepository, times(1)).deleteById(anyLong());
    }
}