package com.example.portfolio.webstorespring.model.dto.orders.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseWithoutDeliveryType {

        private Long id;
        private String deliveryAddress;
        private String shipmentAddress;
}
