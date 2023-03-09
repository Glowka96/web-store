package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
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

    @Mapping(target = "accountDto", source = "account")
    @Mapping(target = "shipmentsDto", source = "shipments")
    OrderDto mapToDto(Order order);

    @Mapping(target = "accountDto", source = "account")
    @Mapping(target = "shipmentsDto", source = "shipments")
    List<OrderDto> mapToDto(List<Order> orders);

    @Mapping(target = "account", source = "accountDto")
    @Mapping(target = "shipments", source = "shipmentsDto")
    Order mapToEntity(OrderDto orderDto);
}
