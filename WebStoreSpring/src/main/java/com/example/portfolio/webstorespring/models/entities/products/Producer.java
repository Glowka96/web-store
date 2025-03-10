package com.example.portfolio.webstorespring.models.entities.products;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "producer")
@NamedEntityGraph(name = "producer-with-products-entity-graph",
        attributeNodes = @NamedAttributeNode("products"))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;
}
