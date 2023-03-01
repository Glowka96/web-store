package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import com.example.porfolio.webstorespring.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AccountRepository accountRepository;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @InjectMocks
    private OrderService underTest;

    private Order order;
    private OrderDto orderDto;
    private AccountDto accountDto;
    private Account account;

    @BeforeEach
    void initialization() {
        ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
        ReflectionTestUtils.setField(orderMapper, "shipmentMapper", shipmentMapper);

        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("Account test");
        accountDto.setLastName("Name");

        account = new Account();
        account.setId(1L);
        account.setFirstName("Account test");
        account.setLastName("Name");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setPrice(10.5);
        productDto.setName("Product Test");

        ShipmentDto shipmentDto = new ShipmentDto();
        shipmentDto.setId(1L);
        shipmentDto.setProductDto(productDto);
        shipmentDto.setQuality(3);
        shipmentDto.setPrice(31.5);

        List<ShipmentDto> shipmentDtos = new ArrayList<>(Arrays.asList(shipmentDto, shipmentDto));

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setShipmentsDto(shipmentDtos);
        orderDto.setStatus(OrderStatus.OPEN);
        orderDto.setAccountDto(accountDto);

        List<Shipment> shipments = shipmentMapper.mapToEntity(shipmentDtos);

        order = new Order();
        order.setId(1L);
        order.setShipments(shipments);
        order.setStatus(OrderStatus.OPEN);
        order.setAccount(account);
    }

    @Test
    void shouldGetOrderDtoById() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        OrderDto orderDto1 = underTest.getOrderDtoById(1L);

        // then
        assertThat(order.getId()).isEqualTo(orderDto1.getId());
    }

    @Test
    void willThrowWhenOrderByIdIsNotFound() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getOrderDtoById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void shouldGetAllOrderDto() {
        // given
        // when
        underTest.getAllOrderDto();

        // then
        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldSave() {
        // given
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));
        String expectedNameUser = accountDto.getFirstName() + " " + accountDto.getLastName();
        Double expectedDouble = 63.0;

        // when
        orderDto = underTest.save(1L, orderDto);

        // then
        assertThat(orderDto.getAccountDto().getId()).isEqualTo(1L);
        assertThat(orderDto.getNameUser()).isEqualTo(expectedNameUser);
        assertThat(orderDto.getProductsPrice()).isEqualTo(expectedDouble);
    }

    @Test
    void shouldUpdate() throws OrderCanNotModifiedException {
        // given
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderDto = underTest.update(1L, 1L, orderDto);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        Order capturedOrder = orderArgumentCaptor.getValue();
        OrderDto mappedOrderDto = orderMapper.mapToDto(capturedOrder);

        assertThat(mappedOrderDto).isEqualTo(orderDto);
    }

    @Test
    void willThrowWhenOrderStatusIsNotOpen() {
        // given
        order.setStatus(OrderStatus.COMPLETED);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.update(1L, 1L, orderDto))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be updated because the order is being prepared");
    }

    @Test
    void shouldDeleteOrderById() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        underTest.deleteOrderById(1L);

        // then
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
}