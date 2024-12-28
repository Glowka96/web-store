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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany
    List<Subscriber> subscriberList;
}
