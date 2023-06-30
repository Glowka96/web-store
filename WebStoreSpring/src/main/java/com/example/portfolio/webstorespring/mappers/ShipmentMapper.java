package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
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

    @Mapping(target = "productResponse", source = "product")
    ShipmentResponse mapToDto(Shipment shipment);

    @Mapping(target = "productResponse", source = "products")
    List<ShipmentResponse> mapToDto(List<Shipment> shipments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", source = "productRequest")
    Shipment mapToEntity(ShipmentRequest shipmentRequest);

    @Mapping(target = "product", source = "productRequest")
    List<Shipment> mapToEntity(List<ShipmentRequest> shipmentResponses);
}
