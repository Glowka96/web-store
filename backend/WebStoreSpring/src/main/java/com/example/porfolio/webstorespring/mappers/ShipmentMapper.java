package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductMapper.class,
        }
)
public interface ShipmentMapper {

    @Mapping(target = "productDto", source = "product")
    @Mapping(target = "orderResponse", source = "order")
    ShipmentDto mapToDto(Shipment shipment);

    @Mapping(target = "orderResponse", source = "order")
    List<ShipmentDto> mapToDto(List<Shipment> all);

    @Mapping(target = "product", source = "productDto")
    @Mapping(target = "order", source = "orderResponse")
    Shipment mapToEntity(ShipmentDto shipmentDto);

    @Mapping(target = "order", source = "orderResponse")
    List<Shipment> mapToEntity(List<ShipmentDto> shipmentDtos);
}
