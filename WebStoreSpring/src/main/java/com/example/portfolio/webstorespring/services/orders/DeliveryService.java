package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeliveryService {

    @Value("${shipment.address}")
    private String shipmentAddress;

    private final DeliveryTypeService deliveryTypeService;

    Delivery formatDelivery(DeliveryRequest deliveryRequest) {
        return Delivery.builder()
                .shipmentAddress(shipmentAddress)
                .deliveryAddress(
                        formatDeliveryAddress(
                                deliveryRequest.getDeliveryAddress().split(", ")))
                .deliveryType(deliveryTypeService.getDeliveryTypeById(
                        deliveryRequest.getDeliveryTypeId()))
                .build();
    }

    private String formatDeliveryAddress(String... address) {
        return "City: " +
               address[0] +
               ", Postcode: " +
               address[1] +
               ", Street: " +
               address[2];
    }
}
