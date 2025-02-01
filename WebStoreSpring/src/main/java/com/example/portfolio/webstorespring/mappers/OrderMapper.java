package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.models.entity.orders.Order;

import java.util.List;


public interface OrderMapper {

    static OrderResponse mapToDto(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getNameUser(),
                order.getTotalPrice(),
                DeliveryMapper.mapToDto(order.getDelivery()),
                order.getCreatedAt(),
                order.getStatus(),
                ShipmentMapper.mapToDto(order.getShipments())
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
                DeliveryMapper.mapToWithoutDeliveryType(order.getDelivery()),
                order.getCreatedAt(),
                order.getStatus(),
                null
        );
    }
}
