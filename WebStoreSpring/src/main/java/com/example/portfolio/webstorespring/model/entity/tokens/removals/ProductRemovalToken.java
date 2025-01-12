package com.example.portfolio.webstorespring.model.entity.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "product_removal_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRemovalToken {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId()
    @JoinColumn(name = "id")
    private ProductSubscriber subscriber;
}
