package com.example.portfolio.webstorespring.models.entities.tokens.removals;

import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "single_product_removal_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleProductRemovalToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_subscriber_id")
    private ProductSubscriber subscriber;
}
