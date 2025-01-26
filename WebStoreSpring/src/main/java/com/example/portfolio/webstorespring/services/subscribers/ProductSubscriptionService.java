package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import com.example.portfolio.webstorespring.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSubscriptionService {

    private final ProductSubscriptionRepository subscriptionRepository;
    private final ProductService productService;

    public ProductSubscription getByProductIdWithEnabledSubscribers(Long productId) {
        log.info("Fetching product subscription for product id {}", productId);
        return subscriptionRepository.findByProductSubscribersEnabled(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", productId));
    }

    @Transactional
    public void add(ProductSubscriber productSubscriber, Long productId) {
        log.info("Adding product subscription for product id {}", productId);
        ProductSubscription productSubscription = subscriptionRepository.findById(productId)
                .orElse(ProductSubscription.builder()
                        .product(productService.findById(productId))
                        .build());
        log.debug("Adding subscriber for product id {}", productId);
        productSubscription.addSubscriber(productSubscriber);
        subscriptionRepository.save(productSubscription);
        log.info("Added subscriber with email: {}, for product id {}", productSubscriber.getEmail(), productId);
    }

    @Transactional
    public void removeForSingleProduct(ProductSubscriber productSubscriber, Long productId) {
        log.info("Removing product subscription for product id {}", productId);
        ProductSubscription productSubscription = subscriptionRepository.findByIdAndSubscriberEmail(productId, productSubscriber.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", productId));
        log.debug("Removing subscriber with email: {}, for product id {}", productSubscriber.getEmail(), productId);
        productSubscription.removeSubscriber(productSubscriber);
        subscriptionRepository.save(productSubscription);
        log.info("Removed subscriber with email: {}, for product id {}", productSubscriber.getEmail(), productId);
    }

    public void deleteById(Long productId) {
        subscriptionRepository.deleteById(productId);
    }
}
