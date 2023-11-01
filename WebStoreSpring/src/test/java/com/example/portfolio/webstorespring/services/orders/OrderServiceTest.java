package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.enums.AccessDeniedExceptionMessage;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.exceptions.OrderCanNotModifiedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.OrderMapper;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ShipmentMapper;
import com.example.portfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @InjectMocks
    private OrderService underTest;

    private Order order;
    private Account account;

    @BeforeEach
    void initialization() {
       ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
       ReflectionTestUtils.setField(orderMapper, "shipmentMapper", shipmentMapper);

       ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
       ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

       ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
       ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        Product product = Product.builder()
                .price(20.00)
                .build();

        Shipment shipment = Shipment.builder()
                .product(product)
                .quantity(1)
                .build();

        List<Shipment> shipments = new ArrayList<>(Arrays.asList(shipment, shipment));

        order = Order.builder()
                .id(1L)
                .shipments(shipments)
                .status(OrderStatus.OPEN)
                .build();

        AccountAddress accountAddress = AccountAddress.builder()
                .city("Test")
                .postcode("99-999")
                .street("test 59/2")
                .build();

        account = Account.builder()
                .id(1L)
                .firstName("test")
                .lastName("dev")
                .email("test@dev.pl")
                .address(accountAddress)
                .orders(Arrays.asList(order, order))
                .build();

        order.setAccount(account);
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAllAccountsOrder() {
        // given
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
        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse orderResponse = underTest.getAccountOrderByOrderId(1L);

        // then
        assertThat(orderResponse.getId()).isEqualTo(order.getId());
        assertThat(orderResponse.getShipmentResponses()).hasSize(2);
    }

    @Test
    void willThrowWhenGetAccountOrderNoOwnAuthAccount() {
        // given
        setupOtherAccountToAuthentication();
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
    void shouldSave() {
        // given
        mockAuthentication();
        String expectedNameUser = account.getFirstName() + " " + account.getLastName();

        OrderRequest orderRequest = createOrderRequest();
        Double expectedDouble = 120.0;

        // when
        OrderResponse savedOrderResponse = underTest.saveOrder(orderRequest);

        // then
        assertThat(expectedNameUser).isEqualTo(savedOrderResponse.getNameUser());
        assertThat(expectedDouble).isEqualTo(savedOrderResponse.getProductsPrice());
        assertThat(savedOrderResponse.getDeliveryAddress()).hasToString("City: Test, Postcode: 99-999, Street: test 59/6");
    }

    @Test
    void shouldUpdate() throws OrderCanNotModifiedException {
        // given
        mockAuthentication();
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse updateOrderResponse = underTest.updateOrder(1L, createOrderRequest());

        // then
        ArgumentCaptor<Order> orderArgumentCaptor =
                ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        Order capturedOrder = orderArgumentCaptor.getValue();
        OrderResponse mappedOrderResponse = orderMapper.mapToDto(capturedOrder);

        assertThat(mappedOrderResponse).isEqualTo(updateOrderResponse);
        assertThat(updateOrderResponse.getDeliveryAddress()).hasToString("City: Test, Postcode: 99-999, Street: test 59/6");
    }

    @Test
    void willThrowWhenUpdateOrderNoOwnAuthAccount() {
        // given
        setupOtherAccountToAuthentication();
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
        mockAuthentication();
        order.setStatus(OrderStatus.COMPLETED);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        // then
        assertThatThrownBy(() -> underTest.updateOrder(1L,  createOrderRequest()))
                .isInstanceOf(OrderCanNotModifiedException.class)
                .hasMessageContaining("The order cannot be update because the order is being prepared");
    }

    @Test
    void shouldDeleteOrderById() {
        // given
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
    void willThrowWhenDeleteOrderNotOwnAuthAccount(){
        // given
        setupOtherAccountToAuthentication();
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
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private void setupOtherAccountToAuthentication() {
        Account otherAccount = Account.builder()
                .id(2L)
                .build();
        order.setAccount(otherAccount);
    }

    private OrderRequest createOrderRequest() {
        ProductRequest productRequest = ProductRequest.builder()
                .price(20.0)
                .build();
        ShipmentRequest shipmentRequest = ShipmentRequest.builder()
                .quantity(3)
                .productRequest(productRequest)
                .build();
        return OrderRequest.builder()
                .deliveryAddress("Test, 99-999, test 59/6")
                .shipmentRequests(Arrays.asList(shipmentRequest, shipmentRequest))
                .build();
    }
}
