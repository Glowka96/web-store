package com.example.portfolio.webstorespring.model.entity.confirmations;

import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "newsletter_confirmation_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterConfToken implements BaseConfToken {

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
    private NewsletterSubscriber subscriber;

    @Override
    public String getToken() {
        return tokenDetails.getToken();
    }
}
