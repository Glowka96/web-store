package com.example.porfolio.webstorespring.model.entity.orders;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nameUser;

    @Column(nullable = false)
    private Double productsPrice;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String shipmentAddress;

    @Column(nullable = false)
    private Date dateOfCreated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shipment> shipments;
}

