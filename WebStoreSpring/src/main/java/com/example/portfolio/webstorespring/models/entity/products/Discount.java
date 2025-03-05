package com.example.portfolio.webstorespring.models.entity.products;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @Column(nullable = false, updatable = false)
    private BigDecimal discountRate;

    @Column(nullable = false)
    private Long quantity;

    @Column(updatable = false)
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(name = "discount_subcategory",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<Subcategory> subcategories;
}
