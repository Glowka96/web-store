package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductMapper.class,
        }
)
public interface ShipmentMapper {

    @Mapping(target = "productDto", source = "product")
    @Mapping(target = "orderDto", source = "order")
    ShipmentDto shipmentsToShipmentsDto(Shipment shipment);


    @Mapping(target = "product", source = "productDto")
    @Mapping(target = "order", source = "orderDto")
    Shipment shipmentDtoToShipment(ShipmentDto shipmentDto);
}
