package com.example.portfolio.webstorespring.models.entities.accounts;

import com.example.portfolio.webstorespring.models.entities.orders.Order;
import com.example.portfolio.webstorespring.models.entities.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements OwnerConfToken {

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

    private String backupEmail;

    @Column(nullable = false)
    private String password;

    private String imageUrl;

    private Boolean enabled;

    @ManyToMany()
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL
    )
    private List<Order> orders;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AuthToken> authTokens;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AccountConfToken> accountConfTokens;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String getName() {
        return "Account";
    }
}
