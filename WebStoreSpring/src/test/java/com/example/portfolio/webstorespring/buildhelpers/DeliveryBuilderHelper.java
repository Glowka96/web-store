package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;

import static com.example.portfolio.webstorespring.buildhelpers.DeliveryTypeBuilderHelper.*;

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

    public static Delivery createDeliveryWithBlankDeliveryAddress() {
        DeliveryType deliveryType = createDeliveryType();
        return Delivery.builder()
                .id(1L)
                .deliveryType(deliveryType)
                .deliveryAddress("")
                .shipmentAddress(SHIPMENT_ADDRESS)
                .build();
    }

    public static DeliveryRequest createDeliveryRequest() {
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest();
        return DeliveryRequest.builder()
                .deliveryAddress(DELIVERY_ADDRESS)
                .deliveryTypeRequest(deliveryTypeRequest)
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
}
