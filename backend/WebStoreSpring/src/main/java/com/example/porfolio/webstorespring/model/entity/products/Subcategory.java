package com.example.porfolio.webstorespring.model.entity.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subcategories")
@NamedEntityGraph(name = "SubCategory.category",
        attributeNodes = @NamedAttributeNode("category"))
@NoArgsConstructor
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @Setter
    @OneToMany(mappedBy = "subcategory")
    private List<Product> products;


    public Subcategory(String name) {
        this.name = name;
    }
}
