package com.example.portfolio.webstorespring.model.entity.confirmations;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "account_confirmation_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountConfToken {

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
}
