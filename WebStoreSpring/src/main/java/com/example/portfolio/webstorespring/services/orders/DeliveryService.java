package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.config.providers.ShipmentAddressProvider;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeliveryService {

    private final ShipmentAddressProvider shipmentAddressProvider;
    private final DeliveryTypeService deliveryTypeService;

    Delivery formatDelivery(DeliveryRequest deliveryRequest) {
        return Delivery.builder()
                .shipmentAddress(shipmentAddressProvider.getAddress())
                .deliveryAddress(
                        formatDeliveryAddress(
                                deliveryRequest.getDeliveryAddress().split(", ")))
                .deliveryType(deliveryTypeService.findById(
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
