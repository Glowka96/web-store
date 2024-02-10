package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.enums.AccessDeniedExceptionMessage;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
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
import org.springframework.transaction.annotation.Transactional;

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
    private final ShipmentService shipmentService;
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

    @Transactional
    public OrderResponse saveOrder(OrderRequest orderRequest) {
        Account loggedAccount = getAccountDetails().getAccount();
        Order order = orderMapper.mapToEntity(orderRequest);

        setupNewOrder(order, loggedAccount, orderRequest.getShipmentRequests());

        orderRepository.save(order);

        return orderMapper.mapToDto(order);
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Order foundOrder = findOrderById(orderId);

        checkOwnerOfOrder(foundOrder, AccessDeniedExceptionMessage.UPDATE);

        checkOrderStatus(foundOrder, "update");

        Order order = orderMapper.mapToEntity(orderRequest);

        setupUpdateOrder(foundOrder, order, foundOrder.getAccount(), orderRequest.getShipmentRequests());
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

    private void setupNewOrder(Order order,
                               Account loggedAccount,
                               List<ShipmentRequest> shipmentRequests) {
        order.setAccount(loggedAccount);
        order.setNameUser(loggedAccount.getFirstName() +
                          " " + loggedAccount.getLastName());
        order.setDateOfCreation(getCurrentDate());
        order.setStatus(OrderStatus.OPEN);

        order.setDelivery(deliveryService.formatDelivery(order.getDelivery(),
                loggedAccount.getAddress()));

        order.setShipments(shipmentService.getSetupShipments(order, shipmentRequests));
        setupTotalPrice(order);
    }

    private void setupUpdateOrder(Order currentOrder,
                                  Order updateOrder,
                                  Account loggedAccount,
                                  List<ShipmentRequest> shipmentRequests) {
        currentOrder.setShipments(
                shipmentService.getSetupShipments(currentOrder, shipmentRequests));

        currentOrder.setDateOfCreation(getCurrentDate());
        currentOrder.setDelivery(deliveryService.formatDelivery(updateOrder.getDelivery(),
                loggedAccount.getAddress()));

        setupTotalPrice(currentOrder);
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
