package com.example.portfolio.webstorespring.models.entities.orders;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "delivery_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @OneToMany(mappedBy = "deliveryType")
    private List<Delivery> deliveries;
}
