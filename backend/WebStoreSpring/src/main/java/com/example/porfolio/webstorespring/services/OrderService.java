package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import com.example.porfolio.webstorespring.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public OrderDto getOrderDtoById(Long id) {
        Order foundOrder = findOrderById(id);
        return orderMapper.mapToDto(foundOrder);
    }

    public List<OrderDto> getAllOrderDto() {
        return orderMapper.mapToDto(orderRepository.findAll());
    }

    public OrderDto save(Long accountId, OrderDto orderDto) {
        Account foundAccount = findAccountById(accountId);
        Order order = orderMapper.mapToEntity(orderDto);

        setupNewOrder(foundAccount, order);

        orderRepository.save(order);
        return orderMapper.mapToDto(order);
    }

    public OrderDto update(Long accountId, Long orderId, OrderDto orderDto) {
        //TODO do this method
        return null;
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }


    private void setupNewOrder(Account account, Order order) {
        order.setNameUser(account.getFirstName() +
                " " + account.getLastName());

        order.setDateOfCreated(Date.from(LocalDateTime.now()
                .atZone(ZoneId.systemDefault()).toInstant()));

        order.setStatus(OrderStatus.OPEN);

        Double productsPrice = order.getShipments()
                .stream()
                .mapToDouble(Shipment::getPrice)
                .sum();
        order.setProductsPrice(productsPrice);
    }

    private void setupOrder(Account foundAccount,
                            Order foundOrder,
                            Order order) {
        order.setId(foundOrder.getId());
        order.setAccount(foundAccount);

    }
}
