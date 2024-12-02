package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

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
import static org.junit.jupiter.api.Assertions.assertNull;
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
    @InjectMocks
    private OrderService underTest;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAllAccountsOrder() {
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findAllByAccountId(anyLong())).willReturn(Arrays.asList(order, order));

        List<OrderResponse> foundOrderResponses = underTest.getAllAccountOrder(accountDetails);

        assertEquals(2, foundOrderResponses.size());
        assertNullShipmentAndDeliveryType(foundOrderResponses);
    }

    @Test
    void shouldGetAccountOrderByOrderId() {
        Order order = createOrder();
        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        OrderResponse orderResponse = underTest.getById(accountDetails,1L);

        assertEquals(order.getId(), orderResponse.id());
        assertThat(orderResponse.shipmentResponses()).hasSize(2);
    }

    @Test
    void shouldGetLastFiveAccountOrders() {
        Order order = createOrder();

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findLastFiveAccountOrder(anyLong()))
                .willReturn(List.of(order, order, order, order, order));

        List<OrderResponse> findOrders = underTest.getLastFiveAccountOrder(accountDetails);

        assertThat(findOrders).hasSize(5);
        assertNullShipmentAndDeliveryType(findOrders);
    }

    @Test
    void willThrowAccessDeniedException_whenGetAccountOrderNoOwnAuthAccount() {
        Order order = createOrder();
        setupOtherAccountToAuthentication(order);

        AccountDetails accountDetails = getAccountDetails();

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> underTest.getById(accountDetails, 1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You can only get your data");
    }

    @Test
    void willThrowResourceNotFoundException_whenGetAccountOrderIdNotFoundOrder() {
        AccountDetails accountDetails = getAccountDetails();

        assertThatThrownBy(() -> underTest.getById(accountDetails, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void shouldSaveOrder() {
        AccountDetails accountDetails = getAccountDetails();

        OrderRequest orderRequest = createOrderRequest();

        Delivery delivery = createDelivery();
        given(deliveryService.formatDelivery(any())).willReturn(delivery);

        OrderResponse savedOrderResponse = underTest.save(accountDetails, orderRequest);

        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        OrderResponse orderResponse = OrderMapper.mapToDto(orderArgumentCaptor.getValue());

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

    private static void assertNullShipmentAndDeliveryType(List<OrderResponse> foundOrderResponses) {
        foundOrderResponses.forEach(orderResponse -> {
            assertNull(orderResponse.shipmentResponses());
            assertNull(orderResponse.deliveryResponse().deliveryTypeResponse());
        });
    }
}
