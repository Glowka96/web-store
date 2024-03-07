package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                DeliveryTypeMapper.class
        }
)
public interface DeliveryMapper {

    @Mapping(target = "deliveryTypeResponse", source = "deliveryType")
    DeliveryResponse mapToDto(Delivery delivery);
}
