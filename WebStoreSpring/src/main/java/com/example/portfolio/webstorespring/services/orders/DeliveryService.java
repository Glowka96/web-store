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

    protected Delivery formatDelivery(DeliveryRequest deliveryRequest) {
        Delivery delivery = Delivery.builder()
                .shipmentAddress(shipmentAddress)
                .deliveryType(deliveryTypeService.getDeliveryTypeById(
                        deliveryRequest.getDeliveryTypeId()))
                .build();

        formatDeliveryAddress(delivery, deliveryRequest.getDeliveryAddress().split(", "));
        return delivery;
    }

    private void formatDeliveryAddress(Delivery delivery, String... address) {
        String formattedAddress = "City: " +
                                  address[0] +
                                  ", Postcode: " +
                                  address[1] +
                                  ", Street: " +
                                  address[2];
        delivery.setDeliveryAddress(formattedAddress);
    }
}
