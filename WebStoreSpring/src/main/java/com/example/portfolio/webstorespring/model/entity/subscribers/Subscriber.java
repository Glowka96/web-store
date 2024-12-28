package com.example.portfolio.webstorespring.model.entity.subscribers;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductSub productSub;

    @ManyToOne(fetch = FetchType.LAZY)
    private NewsletterSub newsletterSub;
}
