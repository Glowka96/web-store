package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ShipmentMapper.class,
                DeliveryMapper.class
        }
)
public interface OrderMapper {

    @Mapping(target = "deliveryResponse", source = "delivery")
    @Mapping(target = "shipmentResponses", source = "shipments")
    OrderResponse mapToDto(Order order);

    @Mapping(target = "deliveryResponse", source = "delivery")
    OrderResponseWithoutShipments mapToDtoWithoutShipments(Order order);

    default List<OrderResponseWithoutShipments> mapToDtoWithoutShipments(List<Order> orders) {
        return orders.stream()
                .map(this::mapToDtoWithoutShipments)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "nameUser", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "delivery", source = "deliveryRequest")
    @Mapping(target = "shipments", ignore = true)
    Order mapToEntity(OrderRequest orderRequest);
}
