package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShipmentService shipmentService;
    private final DeliveryService deliveryService;

    public List<OrderResponseWithoutShipments> getAllAccountOrder(AccountDetails accountDetails) {
        return orderMapper.mapToDtoWithoutShipments(
                orderRepository.findAllByAccountId(
                        accountDetails.getAccount().getId()
                )
        );
    }

    public List<OrderResponseWithoutShipments> getLastFiveAccountOrder(AccountDetails accountDetails) {
        return orderMapper.mapToDtoWithoutShipments(
                orderRepository.findLastFiveAccountOrder(
                        accountDetails.getAccount().getId()
                )
        );
    }

    public OrderResponse getById(AccountDetails accountDetails, Long orderId) {
        Order foundOrder = findById(orderId);

        checkOwnerOfOrder(accountDetails, foundOrder);

        return orderMapper.mapToDto(foundOrder);
    }

    @Transactional
    public OrderResponse save(AccountDetails accountDetails, OrderRequest orderRequest) {
        Account loggedAccount = accountDetails.getAccount();

        Order order = setupOrder(loggedAccount, orderRequest);

        orderRepository.save(order);

        return orderMapper.mapToDto(order);
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
        Order order = Order.builder()
                .account(loggedAccount)
                .nameUser(loggedAccount.getFirstName() +
                          " " + loggedAccount.getLastName())
                .status(OrderStatus.OPEN)
                .shipments(shipmentService.setupShipments(orderRequest.getShipmentRequests(), orderRequest.getDiscountCode()))
                .delivery(deliveryService.formatDelivery(orderRequest.getDeliveryRequest()))
                .build();

        setupTotalPrice(order);
        return order;
    }

    private void setupTotalPrice(Order order) {
        order.setTotalPrice(order.getShipments()
                .stream()
                .map(Shipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(order.getDelivery().getDeliveryType().getPrice())
                .setScale(2, RoundingMode.HALF_UP));
    }
}
