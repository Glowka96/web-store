package com.example.portfolio.webstorespring.model.entity.subscribers;

import com.example.portfolio.webstorespring.model.entity.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "subscriber_products",
            joinColumns = @JoinColumn(name = "product_subscription_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "proudct_subscriber_id", referencedColumnName = "id")
    )
    private Set<ProductSubscriber> productSubscribers;

    public void addSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.add(productSubscriber);
        productSubscriber.getSubscription().add(this);
    }

    public void removeSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.remove(productSubscriber);
        productSubscriber.getSubscription().remove(this);
    }
}
