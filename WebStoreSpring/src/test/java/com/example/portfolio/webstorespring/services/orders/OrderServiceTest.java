package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.DeliveryMapper;
import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.mappers.ShipmentMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
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

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper.createDelivery;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrder;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        // given
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findAllByAccountId(anyLong())).willReturn(Arrays.asList(order, order));

        // when
        List<OrderResponseWithoutShipments> foundOrderResponses = underTest.getAllAccountOrder(accountDetails);

        // then
        assertThat(foundOrderResponses).hasSize(2);
    }

    @Test
    void shouldGetAccountOrderByOrderId() {
        // given
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse orderResponse = underTest.getOrderById(accountDetails,1L);

        // then
        assertThat(orderResponse.getId()).isEqualTo(order.getId());
        assertThat(orderResponse.getShipmentResponses()).hasSize(2);
    }

    @Test
    void shouldGetLastFiveAccountOrders() {
        // given

        Order order = createOrder();

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findLastFiveAccountOrder(anyLong()))
                .willReturn(List.of(order, order, order, order, order));

        // when
        List<OrderResponseWithoutShipments> findOrders = underTest.getLastFiveAccountOrder(accountDetails);

        // then
        assertThat(findOrders).hasSize(5);
    }

    @Test
    void willThrowWhenGetAccountOrderNoOwnAuthAccount() {
        // given
        Order order = createOrder();
        setupOtherAccountToAuthentication(order);

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // than
        assertThatThrownBy(() -> underTest.getOrderById(accountDetails, 1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You can only get your data");
    }

    @Test
    void willThrowWhenGetAccountOrderIdNotFoundOrder() {
        AccountDetails accountDetails = getAccountDetails();

        assertThatThrownBy(() -> underTest.getOrderById(accountDetails, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void shouldSaveOrder() {
        // given
        AccountDetails accountDetails = getAccountDetails();

        OrderRequest orderRequest = createOrderRequest();

        Delivery delivery = createDelivery();
        given(deliveryService.formatDelivery(any())).willReturn(delivery);

        // when
        OrderResponse savedOrderResponse = underTest.saveOrder(accountDetails, orderRequest);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        OrderResponse orderResponse = orderMapper.mapToDto(orderArgumentCaptor.getValue());

        assertThat(savedOrderResponse).isEqualTo(orderResponse);
    }

    private AccountDetails getAccountDetails() {
        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        return new AccountDetails(account);
    }

    private void setupOtherAccountToAuthentication(Order order) {
        Account otherAccount = Account.builder()
                .id(2L)
                .build();
        order.setAccount(otherAccount);
    }
}
