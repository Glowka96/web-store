package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductSubscriberService extends AbstractSubscriberService<ProductSubscriber> {

    public ProductSubscriberService(SubscriberRepository<ProductSubscriber> subscriberRepository) {
        super(subscriberRepository);
    }

    @Override
    protected ProductSubscriber createEntity(SubscriberRequest subscriber) {
        return ProductSubscriber.builder()
                .email(subscriber.email())
                .enabled(Boolean.FALSE)
                .build();
    }
}
