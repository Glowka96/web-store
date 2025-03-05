package com.example.portfolio.webstorespring.models.entity.subscribers;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "newsletter_subscribers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterSubscriber implements OwnerConfToken, Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean enabled;

    @Override
    public String getName() {
        return "Newsletter subscriber";
    }
}
