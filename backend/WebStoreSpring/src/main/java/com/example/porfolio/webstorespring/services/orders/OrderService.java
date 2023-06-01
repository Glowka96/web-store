package com.example.porfolio.webstorespring.services.orders;

import com.example.porfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.porfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.repositories.orders.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AccountRepository accountRepository;
    private final Clock clock = Clock.systemUTC();
    private final static String SHIPMENT_ADDRESS = "City: Lodz, Postcode: 91-473, Street: Julianowska 41/2";

    public List<OrderResponse> getAllOrderDtoByAccountId(Long accountId) {
        List<Order> orders = orderRepository.findAllByAccountId(accountId);
        return orderMapper.mapToDto(orders);
    }

    public OrderResponse getOrderByAccountIdAndOrderId(Long accountId, Long orderId) {
        Order order = findOrderByAccountIdAndOrderId(accountId, orderId);
        return orderMapper.mapToDto(order);
    }

    public OrderResponse saveOrder(Long accountId, OrderRequest orderRequest) {
        Account foundAccount = findAccountById(accountId);
        Order order = orderMapper.mapToEntity(orderRequest);

        setupNewOrder(foundAccount, order);

        orderRepository.save(order);

        return orderMapper.mapToDto(order);
    }

    public OrderResponse updateOrder(Long accountId, Long orderId, OrderRequest orderRequest) {
        Order foundOrder = findOrderById(orderId);

        if (foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException("update");
        }

        Account foundAccount = findAccountById(accountId);
        Order order = orderMapper.mapToEntity(orderRequest);

        setupUpdateOrder(foundAccount, foundOrder, order);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public void deleteOrderById(Long id) {
        Order foundOrder = findOrderById(id);

        if (foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException("delete");
        }
        orderRepository.delete(foundOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private Order findOrderByAccountIdAndOrderId(Long accountId, Long orderId) {
        String msgErrorAccountId = "accountId " + accountId;
        String msgErrorOrderId = "or orderId " + orderId;
        return orderRepository.findOrderByAccountIdAndId(accountId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", msgErrorAccountId, msgErrorOrderId));
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    private void setupNewOrder(Account account, Order order) {
        order.setAccount(account);
        order.setNameUser(account.getFirstName() +
                          " " + account.getLastName());

        order.setDateOfCreated(Date.from(LocalDateTime.now(clock)
                .atZone(ZoneId.systemDefault()).toInstant()));

        order.setStatus(OrderStatus.OPEN);

        if (order.getDeliveryAddress().isEmpty() ||
            order.getDeliveryAddress().isBlank() ||
            order.getDeliveryAddress() == null) {
            String deliveryAddress = account.getAddress().toString();
            order.setDeliveryAddress(deliveryAddress);
        } else {
            formatDeliveryAddress(order);
        }

        order.setShipmentAddress(SHIPMENT_ADDRESS);

        order.getShipments().forEach(shipment -> {
                    shipment.setPrice(
                            BigDecimal.valueOf(
                                            shipment.getQuantity() * shipment.getProduct().getPrice())
                                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
                    shipment.setOrder(order);
                }
        );

        order.setProductsPrice(BigDecimal.valueOf(
                        order.getShipments()
                                .stream()
                                .mapToDouble(Shipment::getPrice)
                                .sum())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
    }

    private void setupUpdateOrder(Account foundAccount,
                                  Order foundOrder,
                                  Order order) {
        order.setId(foundOrder.getId());
        order.setAccount(foundAccount);
        order.setDateOfCreated(Date.from(LocalDateTime.now(clock)
                .atZone(ZoneId.systemDefault()).toInstant()));

        if (order.getNameUser() == null) {
            order.setNameUser(foundAccount.getFirstName() +
                              " " + foundAccount.getLastName());
        }
        if (order.getProductsPrice() == null) {
            order.setProductsPrice(foundOrder.getProductsPrice());
        }
        if (order.getDeliveryAddress() == null) {
            order.setDeliveryAddress(foundOrder.getDeliveryAddress());
        }
        if (order.getShipmentAddress() == null) {
            order.setShipmentAddress(foundOrder.getShipmentAddress());
        } else {
            formatDeliveryAddress(order);
        }
        if (order.getShipments() == null || order.getShipments().isEmpty()) {
            order.setShipments(foundOrder.getShipments());
        }
        if (order.getStatus() == null) {
            order.setStatus(foundOrder.getStatus());
        }
    }

    private void formatDeliveryAddress(Order order) {
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
