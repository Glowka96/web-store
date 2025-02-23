package com.example.portfolio.webstorespring.models.dto.products;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductAvailableEvent extends ApplicationEvent {
    private final Long productId;
    private final String productName;

    public ProductAvailableEvent(Object source, Long productId, String productName) {
        super(source);
        this.productId = productId;
        this.productName = productName;
    }
}
