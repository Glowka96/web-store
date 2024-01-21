package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.enums.AccessDeniedExceptionMessage;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.*;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper.createDelivery;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrder;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DeliveryService deliveryService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @InjectMocks
    private OrderService underTest;

    @BeforeEach
    void initialization() {
        ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
        ReflectionTestUtils.setField(orderMapper, "shipmentMapper", shipmentMapper);

        DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);
        ReflectionTestUtils.setField(orderMapper, "deliveryMapper", deliveryMapper);

        DeliveryTypeMapper deliveryTypeMapper = Mappers.getMapper(DeliveryTypeMapper.class);
        ReflectionTestUtils.setField(deliveryMapper, "deliveryTypeMapper", deliveryTypeMapper);

        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

        ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
        ReflectionTestUtils.setField(productMapper, "productTypeMapper", productTypeMapper);
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAllAccountsOrder() {
        // given
        Order order = createOrder();

        mockAuthentication();
        given(orderRepository.findAllByAccountId(anyLong())).willReturn(Arrays.asList(order, order));

        // when
        List<OrderResponse> foundOrderResponses = underTest.getAllAccountOrder();

        // then
        assertThat(foundOrderResponses).hasSize(2);
    }

    @Test
    void shouldGetAccountOrderByOrderId() {
        // given
        Order order = createOrder();

        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse orderResponse = underTest.getAccountOrderByOrderId(1L);

        // then
        assertThat(orderResponse.getId()).isEqualTo(order.getId());
        assertThat(orderResponse.getShipmentResponses()).hasSize(2);
    }

    @Test
    void shouldGetLastFiveAccountOrders() {
        // given

        Order order = createOrder();

        mockAuthentication();

        given(orderRepository.findLastFiveAccountOrder(anyLong()))
                .willReturn(List.of(order, order, order, order, order));

        // when
        List<OrderResponse> findOrders = underTest.getLastFiveAccountOrder();

        // then
        assertThat(findOrders).hasSize(5);
    }

    @Test
    void willThrowWhenGetAccountOrderNoOwnAuthAccount() {
        // given
        Order order = createOrder();
        setupOtherAccountToAuthentication(order);

        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // than
        assertThatThrownBy(() -> underTest.getAccountOrderByOrderId(1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining(AccessDeniedExceptionMessage.GET.getMessage());
    }

    @Test
    void willThrowWhenGetAccountOrderIdNotFoundOrder() {
        assertThatThrownBy(() -> underTest.getAccountOrderByOrderId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void shouldSaveOrder() {
        // given
        mockAuthentication();

        OrderRequest orderRequest = createOrderRequest();

        Delivery delivery = createDelivery();
        given(deliveryService.formatDelivery(any(), isNull())).willReturn(delivery);
        // when
        OrderResponse savedOrderResponse = underTest.saveOrder(orderRequest);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        OrderResponse orderResponse = orderMapper.mapToDto(orderArgumentCaptor.getValue());

        assertThat(savedOrderResponse).isEqualTo(orderResponse);
    }

    @Test
    void shouldUpdate() throws OrderCanNotModifiedException {
        // given
        Order order = createOrder();
        mockAuthentication();

        given(deliveryService.formatDelivery(any(Delivery.class), any(AccountAddress.class))).willReturn(order.getDelivery());
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse updateOrderResponse = underTest.updateOrder(1L, createOrderRequest());

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        OrderResponse mappedOrderResponse = orderMapper.mapToDto(orderArgumentCaptor.getValue());

        assertThat(mappedOrderResponse).isEqualTo(updateOrderResponse);
    }

    @Test
    void willThrowWhenUpdateOrderNoOwnAuthAccount() {
        // given
        Order order = createOrder();
        setupOtherAccountToAuthentication(order);
        mockAuthentication();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // than
        assertThatThrownBy(() -> underTest.updateOrder(1L, createOrderRequest()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining(AccessDeniedExceptionMessage.UPDATE.getMessage());

    }

    @Test
    void willThrowWhenUpdateOrderStatusIsNotOpen() {
        // given
        Order order = createOrder();
        mockAuthentication();
        order.setStatus(OrderStatus.COMPLETED);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.updateOrder(1L, createOrderRequest()))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be update because the order is being prepared");
    }

    @Test
    void shouldDeleteOrderById() {
        // given
        Order order = createOrder();

        mockAuthentication();
        order.setStatus(OrderStatus.OPEN);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        underTest.deleteOrderById(1L);

        // then
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void willThrowWhenDeleteOrderNotOwnAuthAccount() {
        // given
        Order order = createOrder();

        setupOtherAccountToAuthentication(order);
        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteOrderById(1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining(AccessDeniedExceptionMessage.DELETE.getMessage());
    }

    @Test
    void willThrowWhenDeleteOrderStatusIsNotOpen() {
        Order order = createOrder();
        order.setStatus(OrderStatus.COMPLETED);
        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteOrderById(1L))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be delete because the order is being prepared");
    }

    private void mockAuthentication() {
        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private void setupOtherAccountToAuthentication(Order order) {
        Account otherAccount = Account.builder()
                .id(2L)
                .build();
        order.setAccount(otherAccount);
    }
}
