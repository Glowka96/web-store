package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.orders.responses.OrderResponse;
import com.example.portfolio.webstorespring.models.entities.orders.Order;

import java.util.List;


public interface OrderMapper {

    static OrderResponse mapToDto(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getNameUser(),
                order.getTotalPrice(),
                DeliveryMapper.mapToResponse(order.getDelivery()),
                order.getCreatedAt(),
                order.getStatus(),
                ShipmentMapper.mapToResponse(order.getShipments())
        );
    }

    static List<OrderResponse> mapToDtoWithoutShipments(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::mapToDtoWithoutShipments)
                .toList();
    }

    private static OrderResponse mapToDtoWithoutShipments(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getNameUser(),
                order.getTotalPrice(),
                DeliveryMapper.mapToResponseWithoutDeliveryType(order.getDelivery()),
                order.getCreatedAt(),
                order.getStatus(),
                null
        );
    }
}
