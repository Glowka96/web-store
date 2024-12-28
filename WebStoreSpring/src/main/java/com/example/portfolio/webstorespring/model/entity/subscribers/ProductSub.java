package com.example.portfolio.webstorespring.model.entity.subscribers;

import com.example.portfolio.webstorespring.model.entity.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_subscribers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSub {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productSub", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Subscriber> subscriberList;

    public void addSubscriber(Subscriber subscriber) {
        subscriberList.add(subscriber);
        subscriber.setProductSub(this);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscriberList.remove(subscriber);
        subscriber.setProductSub(null);
    }
}
