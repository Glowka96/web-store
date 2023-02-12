package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public final class OrderDto {

    private Long id;
    @JsonIgnore
    private String nameUser;

    private Double productsPrice;

    private String deliveryAddress;

    private String shipmentAddress;

    @JsonIgnore
    private Date dateOfCreated;

    @JsonIgnore
    private OrderStatus status;

    private AccountDto accountDto;

    private List<ShipmentDto> shipmentsDto;
}
