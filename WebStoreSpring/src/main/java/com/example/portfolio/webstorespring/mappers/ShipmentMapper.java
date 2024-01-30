package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.mapstruct.Context;
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

    @Mapping(target = "product", source = "product")
    ShipmentResponse mapToDto(Shipment shipment);

    List<ShipmentResponse> mapToDto(List<Shipment> shipments);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Shipment mapToEntity(ShipmentRequest shipmentRequest, @Context ProductRepository productRepository);


    List<Shipment> mapToEntity(List<ShipmentRequest> shipmentResponses);
}
