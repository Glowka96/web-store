package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
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

    public static OrderRequest createOrderRequest() {
        ShipmentRequest shipmentRequest = createShipmentRequest();
        DeliveryRequest deliveryRequest = createDeliveryRequest();
        return createOrderRequest(
                List.of(shipmentRequest, shipmentRequest),
                deliveryRequest,
                null
        );
    }

    public static OrderRequest createOrderRequest(ShipmentRequest shipmentRequest,
                                                  DeliveryRequest deliveryRequest,
                                                  String discountCode) {
        return createOrderRequest(
                List.of(shipmentRequest),
                deliveryRequest,
                discountCode
        );
    }

    public static OrderRequest createOrderRequest(List<ShipmentRequest> shipmentRequests,
                                                  DeliveryRequest deliveryRequest,
                                                  String discountCode) {
        return new OrderRequest(
                deliveryRequest,
                shipmentRequests,
                discountCode
        );
    }

    public static OrderResponse createOrderResponse() {
        DeliveryResponse deliveryResponse = createDeliveryResponse();
        ShipmentResponse shipmentResponse = createShipmentResponse();
        return new OrderResponse(
                1L,
                "Name Lastname",
                BigDecimal.valueOf(130.0),
                deliveryResponse,
                LOCAL_DATE_TIME,
                OrderStatus.OPEN,
                List.of(shipmentResponse, shipmentResponse)
        );
    }

    public static OrderResponse createOrderResponseWithoutShipments() {
        DeliveryResponse deliveryResponse = createDeliveryResponseWithoutDeliveryType();
        return new OrderResponse(
                1L,
                "Name Lastname",
                BigDecimal.valueOf(130.0),
                deliveryResponse,
                LOCAL_DATE_TIME,
                OrderStatus.OPEN,
                null
        );
    }
}
