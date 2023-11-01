package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.enums.AccessDeniedExceptionMessage;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final Clock clock = Clock.systemUTC();

    @Value("${shipment.address}")
    private String shipmentAddress;

    public List<OrderResponse> getAllAccountOrder() {
        return orderMapper.mapToDto(
                orderRepository.findAllByAccountId(
                        getAccountDetails()
                                .getAccount()
                                .getId()
                )
        );
    }

    public OrderResponse getAccountOrderByOrderId(Long orderId) {
        Order foundOrder = findOrderById(orderId);

        if(!foundOrder.getAccount().getId().equals(getAccountDetails().getAccount().getId())){
            throw new AccessDeniedException(AccessDeniedExceptionMessage.GET.getMessage());
        }

        return orderMapper.mapToDto(foundOrder);
    }

    public OrderResponse saveOrder(OrderRequest orderRequest) {
        Account loggedAccount = getAccountDetails().getAccount();
        Order order = orderMapper.mapToEntity(orderRequest);

        setupNewOrder(loggedAccount, order);

        orderRepository.save(order);

        return orderMapper.mapToDto(order);
    }

    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Order foundOrder = findOrderById(orderId);

        if(!foundOrder.getAccount().getId().equals(getAccountDetails().getAccount().getId())){
            throw new AccessDeniedException(AccessDeniedExceptionMessage.UPDATE.getMessage());
        }

        if (foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException("update");
        }

        Order order = orderMapper.mapToEntity(orderRequest);

        setupUpdateOrder(foundOrder, order);
        orderRepository.save(foundOrder);
        return orderMapper.mapToDto(foundOrder);
    }

    public void deleteOrderById(Long id) {
        Order foundOrder = findOrderById(id);

        if(!foundOrder.getAccount().getId().equals(getAccountDetails().getAccount().getId())){
            throw new AccessDeniedException(AccessDeniedExceptionMessage.DELETE.getMessage());
        }

        if (foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException("delete");
        }
        orderRepository.delete(foundOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void setupNewOrder(Account loggedAccount, Order order) {
        order.setAccount(loggedAccount);
        order.setNameUser(loggedAccount.getFirstName() +
                          " " + loggedAccount.getLastName());
        order.setDateOfCreation(getCurrentDate());
        order.setStatus(OrderStatus.OPEN);

        if (order.getDeliveryAddress().isEmpty() || order.getDeliveryAddress().isBlank()) {
            order.setDeliveryAddress(loggedAccount.getAddress().toString());
        } else {
            formatDeliveryAddress(order);
        }

        order.setShipmentAddress(shipmentAddress);
        setupPriceAndOrderIdInShipments(order);
        setupTotalPrice(order);
    }

    private void setupUpdateOrder(Order currentOrder, Order updateOrder) {
        setupPriceAndOrderIdInShipments(updateOrder);

        currentOrder.setShipments(updateOrder.getShipments());
        currentOrder.setDateOfCreation(getCurrentDate());
        formatDeliveryAddress(updateOrder);
        currentOrder.setDeliveryAddress(updateOrder.getDeliveryAddress());

        setupTotalPrice(currentOrder);
    }

    private void setupPriceAndOrderIdInShipments(Order order) {
        order.getShipments().forEach(shipment -> {
            shipment.setOrder(order);
            shipment.setPrice(calculateShipmentPrice(shipment));
        });
    }

    private BigDecimal calculateShipmentPrice(Shipment shipment) {
        return BigDecimal.valueOf(shipment.getQuantity())
                .multiply(shipment.getProduct().getPrice())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void setupTotalPrice(Order order) {
        order.setProductsPrice(order.getShipments()
                .stream()
                .map(Shipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP));
    }

    @NotNull
    private Date getCurrentDate() {
        return Date.from(LocalDateTime.now(clock)
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private void formatDeliveryAddress(@NotNull Order order) {
        String[] address = order.getDeliveryAddress().split(", ");
        StringBuilder formattedAddress = new StringBuilder();
        formattedAddress.append("City: ")
                .append(address[0])
                .append(", Postcode: ")
                .append(address[1])
                .append(", Street: ")
                .append(address[2]);
        order.setDeliveryAddress(formattedAddress.toString());
    }
}
