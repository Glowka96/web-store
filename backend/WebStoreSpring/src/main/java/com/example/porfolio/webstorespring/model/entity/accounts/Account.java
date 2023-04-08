package com.example.porfolio.webstorespring.model.entity.accounts;

import com.example.porfolio.webstorespring.model.entity.orders.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "account")
    private AccountAddress address;

    private String imageUrl;

    private Boolean enabled = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRoles accountRoles;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    @OneToMany(mappedBy = "account")
    private List<AuthToken> authTokens;
}
