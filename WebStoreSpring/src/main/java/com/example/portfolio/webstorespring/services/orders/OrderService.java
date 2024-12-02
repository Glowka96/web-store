package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.enums.OrderStatus;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;
    private final DeliveryService deliveryService;

    public List<OrderResponse> getAllAccountOrder(AccountDetails accountDetails) {
        log.info("Fetching all account order for account ID: {}", accountDetails.getAccount().getId());
        return OrderMapper.mapToDtoWithoutShipments(
                orderRepository.findAllByAccountId(
                        accountDetails.getAccount().getId()
                )
        );
    }

    public List<OrderResponse> getLastFiveAccountOrder(AccountDetails accountDetails) {
        log.info("Fetching last five account order for account ID: {}", accountDetails.getAccount().getId());
        return OrderMapper.mapToDtoWithoutShipments(
                orderRepository.findLastFiveAccountOrder(
                        accountDetails.getAccount().getId()
                )
        );
    }

    public OrderResponse getById(AccountDetails accountDetails, Long orderId) {
        log.info("Finding order with ID: {}", orderId);
        Order foundOrder = findById(orderId);

        log.debug("Checking owner of order.");
        checkOwnerOfOrder(accountDetails, foundOrder);

        log.info("Returning found order.");
        return OrderMapper.mapToDto(foundOrder);
    }

    @Transactional
    public OrderResponse save(AccountDetails accountDetails, OrderRequest orderRequest) {
        log.info("Saving order for account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();

        Order order = setupOrder(loggedAccount, orderRequest);

        orderRepository.save(order);
        log.info("Saved order for account ID: {}", accountDetails.getAccount().getId());
        return OrderMapper.mapToDto(order);
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private void checkOwnerOfOrder(AccountDetails accountDetails, Order foundOrder) {
        if (!foundOrder.getAccount().getId().equals(accountDetails.getAccount().getId())) {
            throw new AccessDeniedException("You can only get your data");
        }
    }

    private Order setupOrder(Account loggedAccount,
                             OrderRequest orderRequest) {
        log.debug("Setting up order.");
        Order order = Order.builder()
                .account(loggedAccount)
                .nameUser(loggedAccount.getFirstName() +
                          " " + loggedAccount.getLastName())
                .status(OrderStatus.OPEN)
                .shipments(shipmentService.setupShipments(orderRequest.shipmentRequests(), orderRequest.discountCode()))
                .delivery(deliveryService.formatDelivery(orderRequest.deliveryRequest()))
                .build();

        setupTotalPrice(order);
        log.debug("Set up order.");
        return order;
    }

    private void setupTotalPrice(Order order) {
        log.debug("Setting total price of order.");
        order.setTotalPrice(order.getShipments()
                .stream()
                .map(Shipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getDelivery().getDeliveryType().getPrice())
                .setScale(2, RoundingMode.HALF_UP));
    }
}
