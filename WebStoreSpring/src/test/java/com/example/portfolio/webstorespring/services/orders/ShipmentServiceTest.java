package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrderWithoutShipments;
import static com.example.portfolio.webstorespring.buildhelpers.orders.ShipmentBuilderHelper.createShipmentRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ShipmentRepository shipmentRepository;
    @InjectMocks
    private ShipmentService underTest;

    @Test
    void shouldGetSetupShipments() {
        // given
        Product product = createProduct();
        ShipmentRequest shipmentRequest = createShipmentRequest();
        Order order = createOrderWithoutShipments();
        Long productQuantity = product.getQuantity();

        given(productRepository.findProductsByIdWithPromotion(anyLong())).willReturn(Optional.of(product));

        // when
        List<Shipment> result =  underTest.getSetupShipments(order, List.of(shipmentRequest));

        // then
        assertThat(result).hasSize(1).isNotEmpty();
        assertThat(result.get(0).getOrder()).isEqualTo(order);
        assertThat(result.get(0).getProduct().getQuantity())
                .isNotEqualTo(productQuantity);
    }

    @Test
    void willThrowWhenShipmentQuantityExceedsProductQuantity() {
        // given
        Product product = createProduct();
        ShipmentRequest shipmentRequest = createShipmentRequest();
        shipmentRequest.setQuantity(10000);
        Order order = createOrderWithoutShipments();

        given(productRepository.findProductsByIdWithPromotion(anyLong())).willReturn(Optional.of(product));

        // when
        // then
        assertThatThrownBy(() -> underTest.getSetupShipments(order, List.of(shipmentRequest)))
                .isInstanceOf(ShipmentQuantityExceedsProductQuantityException.class)
                .hasMessageContaining("The shipment quantity exceeds the product quantity");
    }
}
