package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryResponse;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.models.entities.orders.Delivery;
import com.example.portfolio.webstorespring.models.entities.orders.DeliveryType;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryTypeResponse;

public class DeliveryBuilderHelper {

    private static final String DELIVERY_ADDRESS = "Test, 99-999, Test 9";
    private static final String SHIPMENT_ADDRESS = "City, 90-000, Street 9";

    public static Delivery createDelivery() {
        DeliveryType deliveryType = createDeliveryType();
        return Delivery.builder()
                .id(1L)
                .deliveryType(deliveryType)
                .deliveryAddress(DELIVERY_ADDRESS)
                .shipmentAddress(SHIPMENT_ADDRESS)
                .build();
    }

    public static DeliveryRequest createDeliveryRequest() {
        return createDeliveryRequest(1L);
    }

    public static DeliveryRequest createDeliveryRequest(Long deliveryTypeId) {
        return new DeliveryRequest(DELIVERY_ADDRESS, deliveryTypeId);
    }

    public static DeliveryResponse createDeliveryResponse() {
        DeliveryTypeResponse deliveryTypeResponse = createDeliveryTypeResponse();
        return new DeliveryResponse(
                1L,
                DELIVERY_ADDRESS,
                SHIPMENT_ADDRESS,
                deliveryTypeResponse
        );
    }

    public static DeliveryResponse createDeliveryResponseWithoutDeliveryType() {
        return new DeliveryResponse(
                1L,
                DELIVERY_ADDRESS,
                SHIPMENT_ADDRESS,
                null
        );
    }

    public static String getShipmentAddress() {
        return SHIPMENT_ADDRESS;
    }
}
