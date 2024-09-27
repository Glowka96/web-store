package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ProductsNotFoundException;
import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.orders.ShipmentBuilderHelper.createShipmentRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.PRICE_PROMOTIONS;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ShipmentService underTest;

    @Test
    void shouldGetSetupShipments_whenProductHasNotPromotion() {
        // given
        ShipmentRequest shipmentRequest = createShipmentRequest();

        Product product = make(a(BASIC_PRODUCT)
                .but(with(PRICE_PROMOTIONS, Set.of())));
        List<Product> products = List.of(product);
        BigDecimal exceptedPrice = BigDecimal.valueOf(shipmentRequest.getQuantity())
                .multiply(product.getPrice())
                .setScale(2, RoundingMode.HALF_UP);

        given(productRepository.findProductsByIdsWithPromotion(anyList()))
                .willReturn(products);

        // when
        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest));

        // then
        assertThat(result).hasSize(1).isNotEmpty();
        assertThat(result.get(0).getPrice()).isEqualTo(exceptedPrice);
        assertThat(result.get(0).getProduct()).isEqualTo(product);
        assertThat(result.get(0).getQuantity()).isEqualTo(shipmentRequest.getQuantity());
    }

    @Test
    void shouldGetSetupShipments_whenProductHasPromotion() {
        // given
        ShipmentRequest shipmentRequest = createShipmentRequest();
        Product product = make(a(BASIC_PRODUCT));
        List<Product> products = List.of(product);
        BigDecimal exceptedPrice = BigDecimal.valueOf(shipmentRequest.getQuantity())
                .multiply(product.getPricePromotions().iterator().next().getPromotionPrice())
                .setScale(2, RoundingMode.HALF_UP);

        given(productRepository.findProductsByIdsWithPromotion(anyList())).willReturn(products);

        // when
        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest));

        // then
        assertThat(result).hasSize(1).isNotEmpty();
        assertThat(result.get(0).getPrice()).isEqualTo(exceptedPrice);
        assertThat(result.get(0).getProduct()).isEqualTo(product);
        assertThat(result.get(0).getQuantity()).isEqualTo(shipmentRequest.getQuantity());
    }

    @Test
    void willThrowProductsNotFoundException() {
        ShipmentRequest shipmentRequest = createShipmentRequest();


        given(productRepository.findProductsByIdsWithPromotion(anyList())).willReturn(List.of());

        assertThatThrownBy(() -> underTest.setupShipments(List.of(shipmentRequest)))
                .isInstanceOf(ProductsNotFoundException.class)
                .hasMessageContaining("One or more products could not be found");
    }

    @Test
    void willThrowShipmentQuantityExceedsProductQuantityException() {
        // given
        Product product = make(a(BASIC_PRODUCT));
        ShipmentRequest shipmentRequest = createShipmentRequest();
        shipmentRequest.setQuantity(10000);

        given(productRepository.findProductsByIdsWithPromotion(anyList())).willReturn(List.of(product));

        // when
        // then
        assertThatThrownBy(() -> underTest.setupShipments(List.of(shipmentRequest)))
                .isInstanceOf(ShipmentQuantityExceedsProductQuantityException.class)
                .hasMessageContaining("The shipment quantity exceeds the product quantity");
    }
}
