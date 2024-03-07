package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class ShipmentService {

    private final ProductRepository productRepository;

    List<Shipment> getSetupShipments(Order order, List<ShipmentRequest> shipmentRequests) {
        List<Shipment> shipments = new ArrayList<>();
        shipmentRequests.forEach(shipmentRequest ->
                shipments.add(
                        createShipment(
                                order,
                                findProductByIdWithPromotion(shipmentRequest.getProductId()),
                                shipmentRequest.getQuantity())));
        return shipments;
    }

    private Shipment createShipment(Order order, Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new ShipmentQuantityExceedsProductQuantityException();
        }
        return Shipment.builder()
                .product(product)
                .price(calculateShipmentPrice(product, quantity))
                .order(order)
                .quantity(quantity)
                .build();
    }

    private BigDecimal calculateShipmentPrice(Product product, Integer quantity) {
        BigDecimal priceForShipment = product.getPrice();

        if (!product.getPricePromotions().isEmpty()) {
            priceForShipment = product.getPricePromotions()
                    .iterator()
                    .next()
                    .getPromotionPrice();
        }

        product.setQuantity(product.getQuantity() - quantity);
        return BigDecimal.valueOf(quantity)
                .multiply(priceForShipment)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Product findProductByIdWithPromotion(Long productId) {
        return productRepository.findProductByIdWithPromotion(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }
}
