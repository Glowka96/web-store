package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ShipmentMapper.class
        }
)
public interface OrderMapper {

    @Mapping(target = "shipmentResponses", source = "shipments")
    OrderResponse mapToDto(Order order);

    @Mapping(target = "shipmentsResponses", source = "shipments")
    List<OrderResponse> mapToDto(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "shipmentAddress", ignore = true)
    @Mapping(target = "productsPrice", ignore = true)
    @Mapping(target = "nameUser", ignore = true)
    @Mapping(target = "dateOfCreated", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "shipments", source = "shipmentRequests")
    Order mapToEntity(OrderRequest orderRequest);
}
