package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface DeliveryTypeMapper {

    DeliveryTypeResponse mapToDto(DeliveryType deliveryType);

    List<DeliveryTypeResponse> mapToDto(List<DeliveryType> deliveryTypes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    DeliveryType mapToEntity(DeliveryTypeRequest deliveryTypeRequest);


}
