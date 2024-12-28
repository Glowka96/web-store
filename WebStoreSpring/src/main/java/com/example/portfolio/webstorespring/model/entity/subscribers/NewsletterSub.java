package com.example.portfolio.webstorespring.model.entity.subscribers;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "newsletter_subscribers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterSub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String message;

    private LocalDateTime sendDate;

    @OneToMany(mappedBy = "newsletterSub", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Subscriber> subscriberList;

    public void addSubscriber(Subscriber subscriber) {
        subscriberList.add(subscriber);
        subscriber.setNewsletterSub(this);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscriberList.remove(subscriber);
        subscriber.setNewsletterSub(null);
    }
}
