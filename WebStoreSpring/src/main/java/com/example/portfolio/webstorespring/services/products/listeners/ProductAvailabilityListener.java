package com.example.portfolio.webstorespring.services.products.listeners;

import com.example.portfolio.webstorespring.models.dtos.products.ProductAvailableEvent;
import com.example.portfolio.webstorespring.services.emails.notifications.NotifyProductSubscribersService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ProductAvailabilityListener {

    private final NotifyProductSubscribersService notifyProductSubscribersService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductAvailableEvent(ProductAvailableEvent event) {
        notifyProductSubscribersService.notifyEnabledSubscribers(event.getProductId(), event.getProductName());
    }
}
