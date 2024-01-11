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
    private final DeliveryService deliveryService;
    private final Clock clock = Clock.systemUTC();

    public List<OrderResponse> getAllAccountOrder() {
        return orderMapper.mapToDto(orderRepository.findAllByAccountId(
                        getAccountDetails()
                                .getAccount().getId()
                )
        );
    }

    public List<OrderResponse> getLastFiveAccountOrder() {
        return orderMapper.mapToDto(orderRepository.findLastFiveAccountOrder(
                getAccountDetails()
                        .getAccount().getId()
        ));
    }

    public OrderResponse getAccountOrderByOrderId(Long orderId) {
        Order foundOrder = findOrderById(orderId);

        checkOwnerOfOrder(foundOrder, AccessDeniedExceptionMessage.GET);

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

        checkOwnerOfOrder(foundOrder, AccessDeniedExceptionMessage.UPDATE);

        checkOrderStatus(foundOrder, "update");

        Order order = orderMapper.mapToEntity(orderRequest);

        setupUpdateOrder(foundOrder, order, foundOrder.getAccount());
        orderRepository.save(foundOrder);
        return orderMapper.mapToDto(foundOrder);
    }


    public void deleteOrderById(Long id) {
        Order foundOrder = findOrderById(id);

        checkOwnerOfOrder(foundOrder, AccessDeniedExceptionMessage.DELETE);

        checkOrderStatus(foundOrder, "delete");
        orderRepository.delete(foundOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private void checkOwnerOfOrder(Order foundOrder, AccessDeniedExceptionMessage exceptionMessage) {
        if (!foundOrder.getAccount().getId().equals(getAccountDetails().getAccount().getId())) {
            throw new AccessDeniedException(exceptionMessage.getMessage());
        }
    }

    private void checkOrderStatus(Order foundOrder, String update) {
        if (foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException(update);
        }
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

        order.setDelivery(deliveryService.formatDelivery(order.getDelivery(),
                loggedAccount.getAddress()));

        setupShipmentsInOrder(order);
        setupTotalPrice(order);
    }

    private void setupUpdateOrder(Order currentOrder, Order updateOrder, Account loggedAccount) {
        setupShipmentsInOrder(updateOrder);

        currentOrder.setShipments(updateOrder.getShipments());
        currentOrder.setDateOfCreation(getCurrentDate());
        currentOrder.setDelivery(deliveryService.formatDelivery(updateOrder.getDelivery(),
                loggedAccount.getAddress()));

        setupTotalPrice(currentOrder);
    }

    private void setupShipmentsInOrder(Order order) {
        order.getShipments().forEach(shipment -> {
            shipment.setOrder(order);
            shipment.setPrice(calculateShipmentPrice(shipment));
            shipment.getProduct().setQuantity(
                    shipment.getProduct().getQuantity() - shipment.getQuantity());
        });
    }

    private BigDecimal calculateShipmentPrice(Shipment shipment) {
        return BigDecimal.valueOf(shipment.getQuantity())
                .multiply(shipment.getProduct().getPrice())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void setupTotalPrice(Order order) {
        order.setTotalPrice(order.getShipments()
                .stream()
                .map(Shipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(order.getDelivery().getDeliveryType().getPrice())
                .setScale(2, RoundingMode.HALF_UP));
    }

    @NotNull
    private Date getCurrentDate() {
        return Date.from(LocalDateTime.now(clock)
                .atZone(ZoneId.systemDefault()).toInstant());
    }
}
