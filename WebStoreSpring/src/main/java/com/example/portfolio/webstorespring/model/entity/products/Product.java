package com.example.portfolio.webstorespring.model.entity.products;

import com.example.portfolio.webstorespring.enums.ProductType;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
@NamedEntityGraph(name = "product-with-producer-and-price-promotion-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("producer"),
                @NamedAttributeNode("pricePromotions")
        })
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Shipment> shipment;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date dateOfCreation;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductPricePromotion> pricePromotions;
}

