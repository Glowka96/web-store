package com.example.portfolio.webstorespring.model.entity.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_addresses")
@NoArgsConstructor
public class AccountAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
    private String street;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public String toString() {
        return "City: " + city + ", Postcode: " + postcode + ", Street: " + street;
    }
}