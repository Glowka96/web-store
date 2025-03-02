package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.mappers.NewsletterMapper;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsletterMessageService {

    private final NewsletterMessageRepository newsletterMessageRepository;
    private final NotifyNewsletterSubscriberService notifyNewsletterSubscriberService;

    public List<NewsletterMessage> getAllFutureMessages() {
        log.info("Fetching all future messages.");
        return newsletterMessageRepository.findFutureMessages();
    }

    @Transactional
    public ResponseMessageDTO save(NewsletterMessageRequest request) {
        log.info("Saving newsletter message.");
        NewsletterMessage newsletterMessage = NewsletterMapper.mapToEntity(request);
        newsletterMessageRepository.save(newsletterMessage);
        log.info("Saved newsletter message.");
        if(newsletterMessage.getSendDate() == null) {
            notifyNewsletterSubscriberService.notifyEnabledSubscribers(newsletterMessage.getMessage());
            return new ResponseMessageDTO("Newsletter message send to enabled subscribers");
        }
        return new ResponseMessageDTO(String.format("Newsletter message will be send at the date: %s", newsletterMessage.getSendDate()));
    }
}
