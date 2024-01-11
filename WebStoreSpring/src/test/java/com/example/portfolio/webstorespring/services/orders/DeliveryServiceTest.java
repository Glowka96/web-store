package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.DeliveryAddressCanNotEmpty;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.AccountAddressBuilderHelper.createAccountAddress;
import static com.example.portfolio.webstorespring.buildhelpers.DeliveryBuilderHelper.createDelivery;
import static com.example.portfolio.webstorespring.buildhelpers.DeliveryBuilderHelper.createDeliveryWithBlankDeliveryAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService underTest;

    @Test
    void shouldFormatDeliveryWhenDeliveryAddressAndAccountAddressAreNotNull() {
        // given
        Delivery delivery = createDelivery();
        String deliveryAddressBeforeUpdate = delivery.getDeliveryAddress();
        AccountAddress accountAddress = createAccountAddress();

        // when
        Delivery formatedDelivery = underTest.formatDelivery(delivery, accountAddress);

        // then
        assertThat(formatedDelivery.getDeliveryAddress()).isNotEqualTo(deliveryAddressBeforeUpdate);
    }

    @Test
    void shouldFormatDeliveryWhenDeliveryAddressIsNullAndAccountAddressIsNotNull() {
        // given
        Delivery delivery = createDeliveryWithBlankDeliveryAddress();
        AccountAddress accountAddress = createAccountAddress();

        // when
        Delivery formatedDelivery = underTest.formatDelivery(delivery, accountAddress);

        // then
        assertThat(formatedDelivery.getDeliveryAddress())
                .isNotBlank()
                .isNotEmpty()
                .isEqualTo("City: " + accountAddress.getCity() +
                           ", Postcode: " + accountAddress.getPostcode() +
                           ", Street: " + accountAddress.getStreet());
    }

    @Test
    void willThrowWhenDeliveryAddressAndAccountAddressAreNull() {
        // given
        Delivery delivery = createDeliveryWithBlankDeliveryAddress();
        AccountAddress accountAddress = null;

        // when
        // then
        assertThrows(DeliveryAddressCanNotEmpty.class,
                () -> underTest.formatDelivery(delivery, accountAddress));
    }
}
