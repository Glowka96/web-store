package com.example.portfolio.webstorespring.IT.controllers.orders;

import com.example.portfolio.webstorespring.IT.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.IT.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import com.example.portfolio.webstorespring.model.entity.orders.Order;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private InitProductTestData initProductTestData;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;
    private static final String ORDER_URI = "/accounts/orders";
    private Long orderId;
    private Long deliveryTypeId;

    @BeforeEach
    void initTestData() {
        initProductTestData.initTestData();

        Order order1 = creeteOrder(LOCAL_DATE_TIME);
        Order order2 = creeteOrder(LOCAL_DATE_TIME.plusDays(1));
        Order order3 = creeteOrder(LOCAL_DATE_TIME.plusDays(2));
        Order order4 = creeteOrder(LOCAL_DATE_TIME.plusDays(3));
        Order order5 = creeteOrder(LOCAL_DATE_TIME.plusDays(4));
        Order order6 = creeteOrder(LOCAL_DATE_TIME.plusDays(5));
        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6));
        orderId = order1.getId();
        deliveryTypeId = order1.getDelivery().getDeliveryType().getId();
    }

    @AfterEach
    void deleteTestData() {
        orderRepository.deleteAll();
        initProductTestData.deleteTestData();
    }

    @Test
    void shouldGetAllAccountOrders_forAuthenticatedUser_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<List<OrderResponseWithoutShipments>> response = restTemplate.exchange(
                localhostUri + ORDER_URI,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull().hasSize(6);
    }

    @Test
    void shouldGetLastFiveAccountOrders_forAuthenticatedUser_thenStatusOK() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<List<OrderResponseWithoutShipments>> response = restTemplate.exchange(
                localhostUri + ORDER_URI + "/last-five",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull().hasSize(5);
    }

    @Test
    void shouldGetOrderById_forAuthenticatedUser_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<OrderResponse> response = restTemplate.exchange(
                localhostUri + ORDER_URI + "/" + orderId,
                HttpMethod.GET,
                httpEntity,
                OrderResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        assertThat(optionalOrder).isPresent();
        assertThat(response.getBody().getId()).isEqualTo(optionalOrder.get().getId());
        assertThat(response.getBody().getNameUser()).isEqualTo(optionalOrder.get().getNameUser());
        assertThat(response.getBody().getStatus()).isEqualTo(optionalOrder.get().getStatus());
        assertThat(response.getBody().getCreatedAt()).isEqualTo(optionalOrder.get().getCreatedAt());
        assertThat(response.getBody().getTotalPrice().doubleValue())
                .isEqualTo(optionalOrder.get().getTotalPrice().doubleValue());
    }

    @Test
    void shouldSaveOrder_forAuthenticatedUser_thenStatusCreated() {
        OrderRequest orderRequest = OrderRequest.builder()
                .deliveryRequest(DeliveryRequest.builder()
                        .deliveryTypeId(deliveryTypeId)
                        .deliveryAddress("Testcity, 99-999, Teststreet 59/2")
                        .build())
                .shipmentRequests(List.of(ShipmentRequest.builder()
                        .productId(initProductTestData.getProductIdThatHasPromotion())
                        .quantity(1)
                        .build()))
                .build();
        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, getHttpHeaderWithUserToken());

        ProductWithProducerAndPromotionDTO productBeforeSaveOrder = productRepository.findProductById(initProductTestData.getProductIdThatHasPromotion(), LocalDateTime.now().minusDays(30));

        ResponseEntity<OrderResponse> response = restTemplate.exchange(
                localhostUri + ORDER_URI,
                HttpMethod.POST,
                httpEntity,
                OrderResponse.class
        );

        ProductWithProducerAndPromotionDTO productAfterSaveOrder = productRepository.findProductById(initProductTestData.getProductIdThatHasPromotion(), LocalDateTime.now().minusDays(30));
        assertThat(productBeforeSaveOrder.quantity()).isNotEqualTo(productAfterSaveOrder.quantity());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNameUser()).isEqualTo(getSavedUser().getFirstName() + " " + getSavedUser().getLastName());
        assertThat(response.getBody().getStatus()).isEqualTo(OrderStatus.OPEN);
        assertThat(response.getBody().getCreatedAt()).isNotNull();
        assertThat(response.getBody().getShipmentResponses()).hasSize(1);
        assertThat(response.getBody().getDeliveryResponse()).isNotNull();
        assertThat(response.getBody().getTotalPrice().doubleValue())
                .isEqualTo(productAfterSaveOrder.promotionPrice().add(
                        response.getBody().getDeliveryResponse().getDeliveryTypeResponse().getPrice()
                ).doubleValue());
    }

    private Order creeteOrder(LocalDateTime localDataTime) {
        DeliveryType savedDeliveryType = deliveryTypeRepository.save(createDeliveryType());
        Account account = getSavedUser();
        Optional<Product> optionalProduct = productRepository.findById(initProductTestData.getProductIdThatHasNoPromotion());
        BigDecimal productPrice = optionalProduct.get().getPrice();
        String testAddress = "City: Test, Postcode: 99-999, Street: Test 59/2";
        return Order.builder()
                .account(account)
                .nameUser(account.getFirstName() + " " + account.getLastName())
                .shipments(List.of(Shipment.builder()
                        .quantity(1)
                        .product(optionalProduct.get())
                        .price(productPrice)
                        .build()))
                .delivery(Delivery.builder()
                        .shipmentAddress(testAddress)
                        .deliveryAddress(testAddress)
                        .deliveryType(savedDeliveryType)
                        .build())
                .createdAt(localDataTime)
                .totalPrice(savedDeliveryType.getPrice().add(productPrice))
                .status(OrderStatus.OPEN)
                .build();
    }
}
