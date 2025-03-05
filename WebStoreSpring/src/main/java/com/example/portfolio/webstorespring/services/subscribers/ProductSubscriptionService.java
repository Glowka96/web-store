package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductNameView;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import com.example.portfolio.webstorespring.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSubscriptionService {

    private final ProductSubscriptionRepository subscriptionRepository;
    private final ProductService productService;

    public ProductSubscription getWithEnabledSubscribersByProductId(Long productId) {
        log.info("Fetching product subscription for product id {}", productId);
        return subscriptionRepository.findByIdWithEnabledSubscribers(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", productId));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void add(ProductSubscriber subscriber, Long productId) {
        log.info("Adding product subscription for product id {}", productId);
        ProductSubscription productSubscription = subscriptionRepository.findById(productId)
                .orElse(ProductSubscription.builder()
                        .product(productService.findById(productId))
                        .productSubscribers(new HashSet<>())
                        .build());
        log.debug("Adding subscriber for product id {}", productId);
        productSubscription.addSubscriber(subscriber);
        subscriptionRepository.save(productSubscription);
        log.info("Added subscriber with email: {}, for product id {}", subscriber.getEmail(), productId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseMessageDTO removeForSingleProduct(ProductSubscriber subscriber, Long productId) {
        log.info("Removing product subscription for product id {}", productId);
        ProductSubscription productSubscription = subscriptionRepository.findByIdAndSubscriberEmail(productId, subscriber.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", productId));
        log.debug("Removing subscriber with email: {}, for product id {}", subscriber.getEmail(), productId);
        productSubscription.removeSubscriber(subscriber);
        subscriptionRepository.save(productSubscription);
        log.info("Removed subscriber with email: {}, for product id {}", subscriber.getEmail(), productId);
        ProductNameView nameView = productService.getNameById(productId);
        return new ResponseMessageDTO(String.format("Your subscription for this: %s was removed.", nameView.getName()));
    }

    public void deleteById(Long productId) {
        subscriptionRepository.deleteById(productId);
    }
}
