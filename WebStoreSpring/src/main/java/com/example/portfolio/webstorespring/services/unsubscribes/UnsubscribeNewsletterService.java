package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.model.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnsubscribeNewsletterService {

    private final NewsletterRemovalTokenService removalTokenService;
    private final NewsletterSubscriberService subscriberService;

    private static final String RESPONSE_MESSAGE = "Your subscription was removed";

    @Transactional
    public ResponseMessageDTO deleteSubscription(String token) {
        NewsletterRemovalToken removalToken = removalTokenService.getByToken(token);
        subscriberService.deleteById(removalToken.getId());
        removalTokenService.delete(token);
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }
}
