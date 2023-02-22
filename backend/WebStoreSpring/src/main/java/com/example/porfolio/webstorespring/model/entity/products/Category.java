package com.example.porfolio.webstorespring.model.entity.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
@NamedEntityGraph(name = "Category.subcategories",
        attributeNodes = @NamedAttributeNode("subcategories"))
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subcategory> subcategories;

    public Category(String name) {
        this.name = name;
    }
}
