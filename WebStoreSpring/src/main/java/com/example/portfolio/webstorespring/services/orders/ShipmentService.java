package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ProductsNotFoundException;
import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ShipmentService {

    private final ProductRepository productRepository;

    List<Shipment> setupShipments(List<ShipmentRequest> shipmentRequests) {
        Map<Long, Integer> productQuantities = shipmentRequests.stream()
                .collect(Collectors.toMap(ShipmentRequest::getProductId, ShipmentRequest::getQuantity));

        List<Product> products = productRepository.findProductsByIdsWithPromotion(
                List.copyOf(productQuantities.keySet())
        );
        if (products.size() != productQuantities.keySet().size()) {
            throw new ProductsNotFoundException();
        }

        return products.stream()
                .map(p -> createShipment(p, productQuantities.get(p.getId())))
                .toList();
    }

    private Shipment createShipment(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new ShipmentQuantityExceedsProductQuantityException();
        }
        product.setQuantity(product.getQuantity() - quantity);
        return Shipment.builder()
                .product(product)
                .price(calculateShipmentPrice(product, quantity))
                .quantity(quantity)
                .build();
    }

    private BigDecimal calculateShipmentPrice(Product product, Integer quantity) {
        return BigDecimal.valueOf(quantity)
                .multiply(
                        product.getPromotions().stream()
                                .findFirst()
                                .map(Promotion::getPromotionPrice)
                                .orElse(product.getPrice()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
