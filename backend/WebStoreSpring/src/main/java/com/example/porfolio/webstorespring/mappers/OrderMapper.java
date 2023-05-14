package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.porfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
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

    @Mapping(target = "shipmentsDto", source = "shipments")
    OrderResponse mapToDto(Order order);

    @Mapping(target = "shipmentsDto", source = "shipments")
    List<OrderResponse> mapToDto(List<Order> orders);

    @Mapping(target = "shipments", source = "shipmentsDto")
    Order mapToEntity(OrderRequest orderRequest);
}
