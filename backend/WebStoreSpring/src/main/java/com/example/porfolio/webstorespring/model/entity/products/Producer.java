package com.example.porfolio.webstorespring.model.entity.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "producer")
@NamedEntityGraph(name = "Producer.products",
        attributeNodes = @NamedAttributeNode("products"))
@NoArgsConstructor
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
