package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.NewsletterConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.TokenDetailsService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegisterNewsletterSubscriberService extends AbstractConfirmEmailService<NewsletterConfToken, NewsletterSubscriber, NewsletterConfTokenService> {

    private final NewsletterSubscriberService newsletterSubscriberService;
    private final NewsletterRemovalTokenService newsletterRemovalTokenService;

    RegisterNewsletterSubscriberService(EmailSenderService emailSenderService,
                                        NewsletterConfTokenService confirmationTokenService,
                                        TokenDetailsService tokenDetailsService,
                                        NewsletterSubscriberService newsletterSubscriberService,
                                        NewsletterRemovalTokenService newsletterRemovalTokenService) {
        super(emailSenderService, confirmationTokenService, tokenDetailsService);
        this.newsletterSubscriberService = newsletterSubscriberService;
        this.newsletterRemovalTokenService = newsletterRemovalTokenService;
    }

    @Transactional
    public Map<String, Object> register(SubscriberRequest subscriberRequest) {
        NewsletterSubscriber newsletterSubscriber = newsletterSubscriberService.save(subscriberRequest);
        sendConfirmationEmail(newsletterSubscriber, NotificationType.CONFIRM_NEWSLETTER);
        return Map.of(RESPONSE_MESSAGE_KEY, "Verify your email address using the link in your email.");
    }

    @Transactional
    public Map<String, Object> confirm(String token) {
        return confirmTokenOrResend(token, NotificationType.RECONFIRM_NEWSLETTER);
    }

    @Override
    protected void executeAfterConfirm(NewsletterSubscriber ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
        NewsletterRemovalToken newsletterRemovalToken = newsletterRemovalTokenService.save(ownerToken);
        sendEmail(NotificationType.WELCOME_NEWSLETTER, ownerToken.getEmail(), newsletterRemovalToken.getToken());
    }
}
