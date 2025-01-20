package com.example.portfolio.webstorespring.model.entity.subscribers;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product_subscribers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubscriber implements OwnerConfToken, Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean enabled;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "productSubscribers")
    private Set<ProductSubscription> subscription = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSubscriber that = (ProductSubscriber) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String getName() {
        return "Product subscriber";
    }
}
