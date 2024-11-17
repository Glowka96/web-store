package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponseWithoutDeliveryType;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import lombok.Getter;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryTypeResponse;

public class DeliveryBuilderHelper {

    private static final String DELIVERY_ADDRESS = "Test, 99-999, Test 9";
    @Getter
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
        return DeliveryRequest.builder()
                .deliveryAddress(DELIVERY_ADDRESS)
                .deliveryTypeId(1L)
                .build();
    }

    public static DeliveryRequest createDeliveryRequest(Long deliveryTypeId) {
        return DeliveryRequest.builder()
                .deliveryAddress(DELIVERY_ADDRESS)
                .deliveryTypeId(deliveryTypeId)
                .build();
    }

    public static DeliveryResponse createDeliveryResponse() {
        DeliveryTypeResponse deliveryTypeResponse = createDeliveryTypeResponse();
        return DeliveryResponse.builder()
                .id(1L)
                .deliveryTypeResponse(deliveryTypeResponse)
                .deliveryAddress(DELIVERY_ADDRESS)
                .shipmentAddress(SHIPMENT_ADDRESS)
                .build();
    }

    public static DeliveryResponseWithoutDeliveryType createDeliveryResponseWithoutDeliveryType() {
        return DeliveryResponseWithoutDeliveryType.builder()
                .id(1L)
                .deliveryAddress(DELIVERY_ADDRESS)
                .shipmentAddress(SHIPMENT_ADDRESS)
                .build();
    }
}
