package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public final class OrderDto {

    private Long id;

    private String nameUser;

    private Double productsPrice;

    private String deliveryAddress;

    private String shipmentAddress;

    private Date dateOfCreated;

    private OrderStatus status;

    private AccountDto accountDto;

    private List<ShipmentDto> shipmentsDto;
}
