package com.example.porfolio.webstorespring.services.orders;

import com.example.porfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.repositories.orders.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AccountRepository accountRepository;
    private final Clock clock = Clock.systemUTC();

    public List<OrderDto> getAllOrderDtoByAccountId(Long accountId) {
        List<Order> orders = orderRepository.findAllByAccountId(accountId);
        return orderMapper.mapToDto(orders);
    }

    public OrderDto getOrderByAccountIdAndOrderId(Long accountId, Long orderId) {
        Order order = findOrderByAccountIdAndOrderId(accountId, orderId);
        return orderMapper.mapToDto(order);
    }

    public OrderDto saveOrder(Long accountId, OrderDto orderDto) {
        Account foundAccount = findAccountById(accountId);
        Order order = orderMapper.mapToEntity(orderDto);

        setupNewOrder(foundAccount, order);

        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto updateOrder(Long accountId, Long orderId, OrderDto orderDto) {
        Order foundOrder = findOrderById(orderId);

        if(foundOrder.getStatus() != OrderStatus.OPEN) {
            throw new OrderCanNotModifiedException("update");
        }

        Account foundAccount = findAccountById(accountId);
        Order order = orderMapper.mapToEntity(orderDto);

        setupUpdateOrder(foundAccount, foundOrder, order);
        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public void deleteOrderById(Long accountId, Long id) {
        Order foundOrder = findOrderById(id);

        if(foundOrder.getStatus() != OrderStatus.OPEN) {
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

        Double productsPrice = order.getShipments()
                .stream()
                .map(shipment -> BigDecimal.valueOf(shipment.getPrice()))
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        order.setProductsPrice(productsPrice);
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
        }
    }
}
