package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper;
import com.example.portfolio.webstorespring.config.providers.ShipmentAddressProvider;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper.createDeliveryRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryTypeService deliveryTypeService;
    @Mock
    private ShipmentAddressProvider shipmentAddressProvider;
    @InjectMocks
    private DeliveryService underTest;

    @Test
    void shouldFormatDelivery() {
        DeliveryType deliveryType = DeliveryTypeBuilderHelper.createDeliveryType();
        DeliveryRequest deliveryRequest = createDeliveryRequest();
        String deliveryAddressBeforeUpdate = deliveryRequest.deliveryAddress();

        given(deliveryTypeService.findById(anyLong())).willReturn(deliveryType);
        given(shipmentAddressProvider.getAddress()).willReturn(DeliveryBuilderHelper.getShipmentAddress());

        Delivery formatedDelivery = underTest.formatDelivery(deliveryRequest);

        assertNotEquals(deliveryAddressBeforeUpdate, formatedDelivery.getDeliveryAddress());
        assertEquals(deliveryType, formatedDelivery.getDeliveryType());
    }
}
