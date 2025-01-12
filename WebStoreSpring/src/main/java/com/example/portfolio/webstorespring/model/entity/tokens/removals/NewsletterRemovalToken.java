package com.example.portfolio.webstorespring.model.entity.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "newsletter_removal_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterRemovalToken {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId()
    @JoinColumn(name = "id")
    private NewsletterSubscriber subscriber;
}
