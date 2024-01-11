package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.DeliveryAddressCanNotEmpty;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeliveryService {

    @Value("${shipment.address}")
    private String shipmentAddress;

    protected Delivery formatDelivery(Delivery delivery, AccountAddress accountAddress) {
        delivery.setShipmentAddress(shipmentAddress);

        if (delivery.getDeliveryAddress().isEmpty() || delivery.getDeliveryAddress().isBlank()) {
            setupDeliveryAddress(delivery, accountAddress);
            return delivery;
        }

        formatDeliveryAddress(delivery, delivery.getDeliveryAddress().split(", "));
        return delivery;
    }

    private void setupDeliveryAddress(Delivery delivery, AccountAddress accountAddress) {
        if (accountAddress == null) {
            throw new DeliveryAddressCanNotEmpty();
        }
        formatDeliveryAddress(delivery,
                accountAddress.getCity(),
                accountAddress.getPostcode(),
                accountAddress.getStreet());
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
