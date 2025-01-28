package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.model.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnsubscribeNewsletterService {

    private final NewsletterRemovalTokenService removalTokenService;
    private final NewsletterSubscriberService subscriberService;

    @Transactional
    public Map<String, Object> deleteSubscription(String token) {
        NewsletterRemovalToken removalToken = removalTokenService.getByToken(token);
        subscriberService.deleteById(removalToken.getId());
        removalTokenService.delete(token);
        return Map.of("message", "Your subscription was removed");
    }
}
