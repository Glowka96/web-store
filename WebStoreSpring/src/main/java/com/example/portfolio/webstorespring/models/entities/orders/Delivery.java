package com.example.portfolio.webstorespring.models.entities.orders;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "delivery")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private String shipmentAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeliveryType deliveryType;
}
