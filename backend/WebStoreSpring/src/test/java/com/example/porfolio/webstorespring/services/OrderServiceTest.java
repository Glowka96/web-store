package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.porfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.porfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.porfolio.webstorespring.model.entity.orders.Order;
import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.porfolio.webstorespring.services.orders.OrderService;
import com.example.porfolio.webstorespring.services.orders.ShipmentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ShipmentService shipmentService;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @InjectMocks
    private OrderService underTest;

    private Order order;
    private OrderResponse orderResponse;
    private AccountRequest accountRequest;
    private Account account;
    private OrderRequest orderRequest;
    private List<ShipmentResponse> shipmentsDto;

    @BeforeEach
    void initialization() {
        ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
        ReflectionTestUtils.setField(orderMapper, "shipmentMapper", shipmentMapper);

        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        AccountAddress accountAddress = new AccountAddress();
        accountAddress.setCity("Test");
        accountAddress.setPostcode("99-999");
        accountAddress.setStreet("test 59/55");

        accountRequest = new AccountRequest();
        accountRequest.setFirstName("Account test");
        accountRequest.setLastName("Name");

        account = new Account();
        account.setId(1L);
        account.setFirstName("Account test");
        account.setLastName("Name");
        account.setAddress(accountAddress);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(1L);
        productRequest.setPrice(10.5);
        productRequest.setName("Product Test");

        ShipmentResponse shipmentResponse = new ShipmentResponse();
        shipmentResponse.setId(1L);
        shipmentResponse.setProductRequest(productRequest);
        shipmentResponse.setQuantity(3);
        shipmentResponse.setPrice(31.5);

        shipmentsDto = new ArrayList<>(Arrays.asList(shipmentResponse, shipmentResponse));

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setShipmentsDto(shipmentsDto);
        orderResponse.setStatus(OrderStatus.OPEN);
        orderResponse.setDeliveryAddress("Test");

        List<Shipment> shipments = shipmentMapper.mapToEntity(shipmentsDto);

        order = new Order();
        order.setId(1L);
        order.setShipments(shipments);
        order.setStatus(OrderStatus.OPEN);
        order.setAccount(account);

        orderRequest = new OrderRequest();
        orderRequest.setShipmentsDto(shipmentsDto);
        orderRequest.setDeliveryAddress("");
    }

    @Test
    void shouldGetAllAccountsOrder() {
        // given
        given(orderRepository.findAllByAccountId(anyLong())).willReturn(Arrays.asList(order, new Order()));

        // when
        List<OrderResponse> orderResponses = underTest.getAllOrderDtoByAccountId(1L);

        // then
        assertThat(orderResponses).isNotNull();
        assertThat(orderResponses.size()).isEqualTo(2);
    }

    @Test
    void willThrowWhenOrderByIdIsNotFound() {
        // given
        given(orderRepository.findOrderByAccountIdAndId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getOrderByAccountIdAndOrderId(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with accountId 1 or orderId 1 not found");
    }

    @Test
    void shouldSave() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
        String expectedNameUser = accountRequest.getFirstName() + " " + accountRequest.getLastName();
        Double expectedDouble = 63.0;

        // when
        when(shipmentService.save(any(ShipmentResponse.class))).thenReturn(shipmentsDto.get(0));
        orderResponse = underTest.saveOrder(1L, orderRequest);

        // then
        assertThat(orderResponse.getNameUser()).isEqualTo(expectedNameUser);
        assertThat(orderResponse.getProductsPrice()).isEqualTo(expectedDouble);
    }

    @Test
    void shouldUpdate() throws OrderCanNotModifiedException {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        orderResponse = underTest.updateOrder(1L, 1L, orderRequest);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        Order capturedOrder = orderArgumentCaptor.getValue();
        OrderResponse mappedOrderResponse = orderMapper.mapToDto(capturedOrder);

        assertThat(mappedOrderResponse).isEqualTo(orderResponse);
    }

    @Test
    void willThrowWhenUpdateOrderStatusIsNotOpen() {
        // given
        order.setStatus(OrderStatus.COMPLETED);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.updateOrder(1L, 1L, orderRequest))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be update because the order is being prepared");
    }

    @Test
    void shouldDeleteOrderById() {
        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        underTest.deleteOrderById(1L, 1L);

        // then
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void willThrowWhenDeleteOrderStatusIsNotOpen() {
        order.setStatus(OrderStatus.COMPLETED);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteOrderById(1L, 1L))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be delete because the order is being prepared");
    }
}