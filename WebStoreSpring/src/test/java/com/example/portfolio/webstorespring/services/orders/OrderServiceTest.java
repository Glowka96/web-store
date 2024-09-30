package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.DeliveryMapper;
import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.mappers.ShipmentMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.ROLES;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper.createDelivery;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrder;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrderRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DeliveryService deliveryService;
    @Mock
    private ShipmentService shipmentService;
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
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAllAccountsOrder() {
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findAllByAccountId(anyLong())).willReturn(Arrays.asList(order, order));

        List<OrderResponseWithoutShipments> foundOrderResponses = underTest.getAllAccountOrder(accountDetails);

        assertEquals(2, foundOrderResponses.size());
    }

    @Test
    void shouldGetAccountOrderByOrderId() {
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        OrderResponse orderResponse = underTest.getOrderById(accountDetails,1L);

        assertEquals(order.getId(), orderResponse.getId());
        assertThat(orderResponse.getShipmentResponses()).hasSize(2);
    }

    @Test
    void shouldGetLastFiveAccountOrders() {
        Order order = createOrder();

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findLastFiveAccountOrder(anyLong()))
                .willReturn(List.of(order, order, order, order, order));

        List<OrderResponseWithoutShipments> findOrders = underTest.getLastFiveAccountOrder(accountDetails);

        assertThat(findOrders).hasSize(5);
    }

    @Test
    void willThrowAccessDeniedException_whenGetAccountOrderNoOwnAuthAccount() {
        Order order = createOrder();
        setupOtherAccountToAuthentication(order);

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> underTest.getOrderById(accountDetails, 1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You can only get your data");
    }

    @Test
    void willThrowResourceNotFoundException_whenGetAccountOrderIdNotFoundOrder() {
        AccountDetails accountDetails = getAccountDetails();

        assertThatThrownBy(() -> underTest.getOrderById(accountDetails, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void shouldSaveOrder() {
        AccountDetails accountDetails = getAccountDetails();

        OrderRequest orderRequest = createOrderRequest();

        Delivery delivery = createDelivery();
        given(deliveryService.formatDelivery(any())).willReturn(delivery);

        OrderResponse savedOrderResponse = underTest.saveOrder(accountDetails, orderRequest);

        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        OrderResponse orderResponse = orderMapper.mapToDto(orderArgumentCaptor.getValue());

        assertEquals(orderResponse, savedOrderResponse);
    }

    private AccountDetails getAccountDetails() {
        Role role = RoleBuilderHelper.createUserRole();
        return new AccountDetails(make(a(BASIC_ACCOUNT)
                .but(with(ROLES, Set.of(role)))));
    }

    private void setupOtherAccountToAuthentication(Order order) {
        Account otherAccount = Account.builder()
                .id(2L)
                .build();
        order.setAccount(otherAccount);
    }
}
