package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ProductsNotFoundException;
import com.example.portfolio.webstorespring.exceptions.ShipmentQuantityExceedsProductQuantityException;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.services.products.DiscountService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ShipmentService {

    private final ProductRepository productRepository;
    private final DiscountService discountService;

    List<Shipment> setupShipments(List<ShipmentRequest> shipmentRequests, String discountCode) {
        Map<Long, Integer> productQuantities = shipmentRequests.stream()
                .collect(Collectors.toMap(ShipmentRequest::productId, ShipmentRequest::quantity));

        List<Product> products = productRepository.findWithPromotionByIds(
                List.copyOf(productQuantities.keySet())
        );

        validateShipment(products, productQuantities);

        return discountCode != null
                ? getCalculatedShipmentsWithDiscount(discountCode, products, productQuantities)
                : getCalculatedShipmentsWithoutDiscount(products, productQuantities);
    }

    private static void validateShipment(List<Product> products, Map<Long, Integer> productQuantities) {
        if (products.size() != productQuantities.keySet().size()) {
            throw new ProductsNotFoundException();
        }

        products.forEach(product -> {
            if (product.getQuantity() < productQuantities.get(product.getId())) {
                throw new ShipmentQuantityExceedsProductQuantityException(productQuantities.get(product.getId()), product.getName());
            }
        });
    }

    @NotNull
    private List<Shipment> getCalculatedShipmentsWithDiscount(String discountCode,
                                                              List<Product> products,
                                                              Map<Long, Integer> productQuantities) {
        Discount discount = discountService.applyByCode(discountCode);
        List<Long> subcategoryIdsWithDiscount = discount.getSubcategories().stream().map(Subcategory::getId).toList();
        BigDecimal multipleDiscountRate = BigDecimal.ONE.subtract(discount.getDiscountRate());

        Map<Boolean, List<Product>> partitionProductsByDiscount = getPartitionProductsByDiscount(products, subcategoryIdsWithDiscount);

        List<Shipment> shipments = new ArrayList<>(
                partitionProductsByDiscount.get(true)
                        .stream()
                        .map(p -> createShipmentPriceWithDiscount(p, productQuantities.get(p.getId()), multipleDiscountRate))
                        .toList()
        );
        shipments.addAll(getCalculatedShipmentsWithoutDiscount(
                partitionProductsByDiscount.get(false), productQuantities)
        );
        return shipments;
    }

    private Map<Boolean, List<Product>> getPartitionProductsByDiscount(List<Product> products, List<Long> subcategoryIdsWithDiscount) {
        return products.stream()
                .collect(Collectors.partitioningBy(p -> subcategoryIdsWithDiscount.contains(p.getSubcategory().getId()) && p.getPromotions().isEmpty()));
    }

    @NotNull
    private List<Shipment> getCalculatedShipmentsWithoutDiscount(List<Product> products, Map<Long, Integer> productQuantities) {
        return products.stream()
                .map(p -> createShipment(p, productQuantities.get(p.getId())))
                .toList();
    }

    private Shipment createShipment(Product product, Integer quantity) {
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

    private Shipment createShipmentPriceWithDiscount(Product product, Integer quantity, BigDecimal multipleDiscountRate) {
        product.setQuantity(product.getQuantity() - quantity);
        return Shipment.builder()
                .product(product)
                .price(calculateShipmentPriceWithDiscount(product, quantity, multipleDiscountRate))
                .quantity(quantity)
                .build();
    }

    private BigDecimal calculateShipmentPriceWithDiscount(Product product, Integer quantity, BigDecimal multipleDiscountRate) {
        return BigDecimal.valueOf(quantity)
                .multiply(product.getPrice().multiply(multipleDiscountRate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
