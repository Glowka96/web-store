package com.example.portfolio.webstorespring.model.entity.subscribers;

import com.example.portfolio.webstorespring.model.entity.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_subscriptions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubscription {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductSubscriber> productSubscribers;

    public void addSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.add(productSubscriber);
        productSubscriber.setSubscription(this);
    }

    public void removeSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.remove(productSubscriber);
        productSubscriber.setSubscription(this);
    }
}
