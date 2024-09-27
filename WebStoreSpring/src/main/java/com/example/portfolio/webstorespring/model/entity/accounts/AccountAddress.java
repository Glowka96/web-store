package com.example.portfolio.webstorespring.model.entity.accounts;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "account_addresses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddress {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postcode;

    @Column(nullable = false)
    private String street;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId()
    @JoinColumn(name = "id")
    private Account account;
}
