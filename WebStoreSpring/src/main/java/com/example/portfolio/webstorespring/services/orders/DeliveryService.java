package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.configs.providers.ShipmentAddressProvider;
import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryRequest;
import com.example.portfolio.webstorespring.models.entities.orders.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class DeliveryService {

    private final ShipmentAddressProvider shipmentAddressProvider;
    private final DeliveryTypeService deliveryTypeService;

    Delivery formatDelivery(DeliveryRequest request) {
        log.info("Returning delivery.");
        return Delivery.builder()
                .shipmentAddress(shipmentAddressProvider.getAddress())
                .deliveryAddress(
                        formatDeliveryAddress(request))
                .deliveryType(deliveryTypeService.findById(
                        request.deliveryTypeId()))
                .build();
    }

    private String formatDeliveryAddress(DeliveryRequest request) {
        return "City: " +
               request.city() +
               ", Postcode: " +
               request.postcode() +
               ", Street: " +
               request.street();
    }
}
