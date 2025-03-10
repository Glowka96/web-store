package com.example.portfolio.webstorespring.models.entities.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "product_confirmation_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfToken implements ConfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Embedded
    private TokenDetails tokenDetails;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "subscriber_id"
    )
    private ProductSubscriber subscriber;

    @Override
    public String getToken() {
        return tokenDetails.getToken();
    }
}
