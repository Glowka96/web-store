package com.example.portfolio.webstorespring.models.entity.subscribers;

import com.example.portfolio.webstorespring.models.entity.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
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
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "subscriptions",
            joinColumns = @JoinColumn(name = "product_subscription_id", referencedColumnName = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_subscriber_id", referencedColumnName = "id")
    )
    private Set<ProductSubscriber> productSubscribers = new HashSet<>();

    public void addSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.add(productSubscriber);
        productSubscriber.getSubscription().add(this);
    }

    public void removeSubscriber(ProductSubscriber productSubscriber) {
        productSubscribers.remove(productSubscriber);
        productSubscriber.getSubscription().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSubscription that = (ProductSubscription) o;
        return Objects.equals(product.getId(), that.product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(product.getId());
    }
}
