package com.example.portfolio.webstorespring.models.entities.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "account_confirmation_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountConfToken implements ConfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Embedded
    private TokenDetails tokenDetails;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "account_id"
    )
    private Account account;

    @Override
    public String getToken() {
        return tokenDetails.getToken();
    }
}
