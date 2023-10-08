package com.example.portfolio.webstorespring.model.entity.accounts;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Table(name = "roles")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;
}
