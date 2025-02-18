package com.example.portfolio.webstorespring.services.emails.registrations;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.NewsletterConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.NewsletterRemovalTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterNewsletterSubscriberService extends AbstractConfirmEmailService<NewsletterConfToken, NewsletterSubscriber, NewsletterConfTokenService> {

    private final NewsletterSubscriberService newsletterSubscriberService;
    private final NewsletterRemovalTokenService newsletterRemovalTokenService;

    private static final String RESPONSE_MESSAGE = "Verify your email address using the link in your email.";

    RegisterNewsletterSubscriberService(EmailSenderService emailSenderService,
                                        NewsletterConfTokenService confirmationTokenService,
                                        NewsletterSubscriberService newsletterSubscriberService,
                                        NewsletterRemovalTokenService newsletterRemovalTokenService) {
        super(emailSenderService, confirmationTokenService);
        this.newsletterSubscriberService = newsletterSubscriberService;
        this.newsletterRemovalTokenService = newsletterRemovalTokenService;
    }

    @Transactional
    public ResponseMessageDTO register(SubscriberRequest request) {
        NewsletterSubscriber newsletterSubscriber = newsletterSubscriberService.save(request);
        sendConfirmationEmail(newsletterSubscriber, EmailType.CONFIRM_NEWSLETTER);
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }

    @Transactional
    public ResponseMessageDTO confirm(String token) {
        return confirmTokenOrResend(token, EmailType.RECONFIRM_NEWSLETTER);
    }

    @Override
    protected void executeAfterConfirm(NewsletterSubscriber ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
        NewsletterRemovalToken newsletterRemovalToken = newsletterRemovalTokenService.save(ownerToken);
        sendEmail(EmailType.WELCOME_NEWSLETTER, ownerToken.getEmail(), newsletterRemovalToken.getToken());
    }
}
