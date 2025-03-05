package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.products.DiscountBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ProductsNotFoundException;
import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.models.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.models.entity.orders.Shipment;
import com.example.portfolio.webstorespring.models.entity.products.Discount;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.services.products.DiscountService;
import org.jetbrains.annotations.NotNull;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountService discountService;
    @InjectMocks
    private ShipmentService underTest;

    @Test
    void shouldGetSetupShipments_whenProductHasNotPromotionAndNotUseDiscount() {
        ShipmentRequest shipmentRequest = createShipmentRequest();

        List<Product> products = getProductListWhatProductHasNoPromotion();
        BigDecimal exceptedPrice = BigDecimal.valueOf(shipmentRequest.quantity())
                .multiply(products.get(0).getPrice())
                .setScale(2, RoundingMode.HALF_UP);

        given(productRepository.findWithPromotionByIds(anyList()))
                .willReturn(products);

        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest), null);

        assertShipment(exceptedPrice, products.get(0), shipmentRequest.quantity(), result);
    }

    @Test
    void shouldGetSetupShipments_whenProductHasPromotionAndNotUseDiscount() {
        ShipmentRequest shipmentRequest = createShipmentRequest();

        List<Product> products = getProductListWhatProductHasPromotion();
        BigDecimal exceptedPrice = getExceptedPriceWhenProductHasPromotion(shipmentRequest, products);

        given(productRepository.findWithPromotionByIds(anyList())).willReturn(products);

        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest), null);

        assertShipment(exceptedPrice, products.get(0), shipmentRequest.quantity(), result);
    }

    @Test
    void shouldGetSetupShipments_whenProductHasNotPromotionAndUseDiscountCode() {
        ShipmentRequest shipmentRequest = createShipmentRequest();
        Discount discount = DiscountBuilderHelper.createDiscount();

        List<Product> products = getProductListWhatProductHasNoPromotion();
        BigDecimal exceptedPrice = BigDecimal.valueOf(shipmentRequest.quantity())
                .multiply(products.get(0).getPrice().multiply(BigDecimal.ONE.subtract(discount.getDiscountRate())))
                .setScale(2, RoundingMode.HALF_UP);

        given(productRepository.findWithPromotionByIds(anyList())).willReturn(products);
        given(discountService.applyByCode(any())).willReturn(discount);

        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest), discount.getCode());

        assertShipment(exceptedPrice, products.get(0), shipmentRequest.quantity(), result);
    }

    @Test
    void shouldGetSetupShipments_whenProductHasPromotionAndUseDiscountCode() {
        ShipmentRequest shipmentRequest = createShipmentRequest();
        Discount discount = DiscountBuilderHelper.createDiscount();

        List<Product> products = getProductListWhatProductHasPromotion();
        BigDecimal exceptedPrice = getExceptedPriceWhenProductHasPromotion(shipmentRequest, products);

        given(productRepository.findWithPromotionByIds(anyList())).willReturn(products);
        given(discountService.applyByCode(any())).willReturn(discount);

        List<Shipment> result = underTest.setupShipments(List.of(shipmentRequest), discount.getCode());

        assertShipment(exceptedPrice, products.get(0), shipmentRequest.quantity(), result);
    }


    private List<Product> getProductListWhatProductHasNoPromotion() {
        return List.of(make(a(BASIC_PRODUCT)
                .but(with(PRICE_PROMOTIONS, Set.of()))));
    }

    private List<Product> getProductListWhatProductHasPromotion() {
        return List.of(make(a(BASIC_PRODUCT)));
    }

    @NotNull
    private static BigDecimal getExceptedPriceWhenProductHasPromotion(ShipmentRequest shipmentRequest, List<Product> products) {
        return BigDecimal.valueOf(shipmentRequest.quantity())
                .multiply(products.get(0).getPromotions().iterator().next().getPromotionPrice())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void assertShipment(BigDecimal exceptedPrice, Product product, Integer exceptedQuantity, List<Shipment> result) {
        assertThat(result).isNotEmpty().hasSize(1);
        assertEquals(exceptedPrice, result.get(0).getPrice());
        assertEquals(product, result.get(0).getProduct());
        assertEquals(exceptedQuantity, result.get(0).getQuantity());
    }

    @Test
    void willThrowProductsNotFoundException_whenShipmentHasProductWhatNotExist() {
        ShipmentRequest shipmentRequest = createShipmentRequest();


        given(productRepository.findWithPromotionByIds(anyList())).willReturn(List.of());

        assertThrows(ProductsNotFoundException.class, () -> underTest.setupShipments(List.of(shipmentRequest), null));
    }

    @Test
    void willThrowShipmentQuantityExceedsProductQuantityException() {
        Product product = make(a(BASIC_PRODUCT));
        ShipmentRequest shipmentRequest = new ShipmentRequest(1L, 1000);

        given(productRepository.findWithPromotionByIds(anyList())).willReturn(List.of(product));

        assertThrows(ShipmentQuantityExceedsProductQuantityException.class,
                () -> underTest.setupShipments(List.of(shipmentRequest), null));
    }
}
