package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.NewsletterRemovalTokenBuilderHelper.createNewsletterRemovalTokenWithEnabledSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnsubscribeNewsletterServiceTest {

    @Mock
    private NewsletterRemovalTokenService newsletterRemovalTokenService;
    @Mock
    private NewsletterSubscriberService newsletterSubscriberService;
    @InjectMocks
    private UnsubscribeNewsletterService underTest;

    @Test
    void shouldDeleteSubscription() {
        NewsletterRemovalToken removalToken = createNewsletterRemovalTokenWithEnabledSubscriber();
        ResponseMessageDTO response = new ResponseMessageDTO("Your subscription was removed");

        given(newsletterRemovalTokenService.getByToken(anyString())).willReturn(removalToken);

        ResponseMessageDTO result = underTest.deleteSubscription("token123");

        verify(newsletterSubscriberService, times(1)).deleteById(removalToken.getId());
        verify(newsletterRemovalTokenService, times(1)).deleteByToken(removalToken.getToken());
        assertEquals(response, result);
    }
}