package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.*;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;

import java.math.BigDecimal;
import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.orders.ShipmentBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class OrderBuilderHelper {

    public static Order createOrder() {
        Account account = make(a(BASIC_ACCOUNT));
        Delivery delivery = createDelivery();
        Shipment shipment = createShipment();
        return Order.builder()
                .id(1L)
                .shipments(List.of(shipment, shipment))
                .status(OrderStatus.OPEN)
                .account(account)
                .delivery(delivery)
                .build();
    }

    public static Order createOrderWithoutShipments() {
        Account account = make(a(BASIC_ACCOUNT));
        Delivery delivery = createDelivery();
        return Order.builder()
                .id(1L)
                .status(OrderStatus.OPEN)
                .account(account)
                .delivery(delivery)
                .build();
    }

    public static OrderRequest createOrderRequest() {
        ShipmentRequest shipmentRequest = createShipmentRequest();
        DeliveryRequest deliveryRequest = createDeliveryRequest();
        return OrderRequest.builder()
                .deliveryRequest(deliveryRequest)
                .shipmentRequests(List.of(shipmentRequest, shipmentRequest))
                .build();
    }

    public static OrderResponse createOrderResponse() {
        DeliveryResponse deliveryResponse = createDeliveryResponse();
        ShipmentResponse shipmentResponse = createShipmentResponse();
        return OrderResponse.builder()
                .id(1L)
                .status(OrderStatus.OPEN)
                .deliveryResponse(deliveryResponse)
                .shipmentResponses(List.of(shipmentResponse, shipmentResponse))
                .nameUser("Name Lastname")
                .totalPrice(BigDecimal.valueOf(130.0))
                .createdAt(LOCAL_DATE_TIME)
                .build();
    }

    public static OrderResponseWithoutShipments createOrderResponseWithoutShipments() {
        DeliveryResponseWithoutDeliveryType deliveryResponse = createDeliveryResponseWithoutDeliveryType();
        return OrderResponseWithoutShipments.builder()
                .id(1L)
                .status(OrderStatus.OPEN)
                .deliveryResponse(deliveryResponse)
                .nameUser("Name Lastname")
                .totalPrice(BigDecimal.valueOf(130.0))
                .createdAt(LOCAL_DATE_TIME)
                .build();
    }
}
