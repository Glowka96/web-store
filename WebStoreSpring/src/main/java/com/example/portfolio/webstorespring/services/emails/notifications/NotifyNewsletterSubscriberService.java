package com.example.portfolio.webstorespring.services.emails.notifications;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyNewsletterSubscriberService {

    private final EmailSenderService emailSenderService;
    private final NewsletterSubscriberService subscriberService;

    public void notifyEnabledSubscribers(String message) {
        subscriberService.getAllEnabled().forEach(s ->
                emailSenderService.sendEmail(
                        EmailType.NEWSLETTER_MESSAGE,
                        s.getEmail(),
                        message
                )
        );
    }
}
