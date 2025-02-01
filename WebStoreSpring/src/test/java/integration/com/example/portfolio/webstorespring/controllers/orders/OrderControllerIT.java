package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.example.portfolio.webstorespring.models.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.models.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.orders.Delivery;
import com.example.portfolio.webstorespring.models.entity.orders.DeliveryType;
import com.example.portfolio.webstorespring.models.entity.orders.Order;
import com.example.portfolio.webstorespring.models.entity.orders.Shipment;
import com.example.portfolio.webstorespring.models.entity.products.Discount;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.productsTestData.InitProductTestData;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import com.example.portfolio.webstorespring.repositories.orders.OrderRepository;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.createOrderRequest;
import static com.example.portfolio.webstorespring.buildhelpers.orders.ShipmentBuilderHelper.createShipmentRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private InitProductTestData initProductTestData;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;
    private static final String ORDER_URI = "/accounts/orders";
    private Long orderId;
    private Long deliveryTypeId;
    private Discount discount;

    @BeforeEach
    void initTestData() {
        initProductTestData.initTestData();

        Account account = getSavedUser();
        DeliveryType savedDeliveryType = deliveryTypeRepository.save(createDeliveryType());
        Optional<Product> optionalProduct = productRepository.findById(initProductTestData.getProductIdThatHasNoPromotion());

        Order order1 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME);
        Order order2 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME.plusDays(1));
        Order order3 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME.plusDays(2));
        Order order4 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME.plusDays(3));
        Order order5 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME.plusDays(4));
        Order order6 = creeteOrder(account, savedDeliveryType, optionalProduct.get(), LOCAL_DATE_TIME.plusDays(5));
        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6));
        orderId = order1.getId();
        deliveryTypeId = order1.getDelivery().getDeliveryType().getId();

        discount = discountRepository.save(Discount.builder()
                .code("test1")
                .quantity(1L)
                .discountRate(BigDecimal.valueOf(0.10))
                .subcategories(Set.of(subcategoryRepository.findById(initProductTestData.getSubId()).get()))
                .build()
        );
    }

    @AfterEach
    void deleteTestData() {
        orderRepository.deleteAll();
        discountRepository.deleteAll();
        initProductTestData.deleteTestData();
    }

    @Test
    void shouldGetAllAccountOrders_forAuthenticatedUser_thenStatusOk() {
        HttpEntity<?> httpEntity = new HttpEntity<>(getHttpHeaderWithUserToken());

        ResponseEntity<List<OrderResponse>> response = restTemplate.exchange(
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

        ResponseEntity<List<OrderResponse>> response = restTemplate.exchange(
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
        assertEquals(optionalOrder.get().getId(), response.getBody().id());
        assertEquals(optionalOrder.get().getNameUser(), response.getBody().nameUser());
        assertEquals(optionalOrder.get().getStatus(), response.getBody().status());
        assertEquals(optionalOrder.get().getCreatedAt(), response.getBody().createdAt());
        assertEquals(optionalOrder.get().getTotalPrice(), response.getBody().totalPrice());
    }

    @Test
    void shouldSaveOrder_whenUserNotUseDiscount_forAuthenticatedUser_thenStatusCreated() {
        OrderRequest orderRequest = createOrderRequest(
                createShipmentRequest(initProductTestData.getProductIdThatHasPromotion()),
                DeliveryBuilderHelper.createDeliveryRequest(deliveryTypeId),
                null
        );

        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, getHttpHeaderWithUserToken());

        ProductWithProducerAndPromotionDTO productBeforeSaveOrder = getProductById(initProductTestData.getProductIdThatHasPromotion());

        ResponseEntity<OrderResponse> response = sendPostRequest(httpEntity);

        ProductWithProducerAndPromotionDTO productAfterSaveOrder = getProductById(initProductTestData.getProductIdThatHasPromotion());

        assertNotEquals(productBeforeSaveOrder.quantity(), productAfterSaveOrder.quantity());

        assertsOrders(response, 1L, productAfterSaveOrder.promotionPrice());
    }

    @Test
    void shouldSaveOrder_whenUserUseDiscount_forAuthenticatedUser_thenStatusCreated() {
        OrderRequest orderRequest = createOrderRequest(
                createShipmentRequest(initProductTestData.getProductIdThatHasNoPromotion()),
                DeliveryBuilderHelper.createDeliveryRequest(deliveryTypeId),
                "test1"
        );

        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, getHttpHeaderWithUserToken());

        ProductWithProducerAndPromotionDTO productBeforeSaveOrder = getProductById(initProductTestData.getProductIdThatHasNoPromotion());

        ResponseEntity<OrderResponse> response = sendPostRequest(httpEntity);

        ProductWithProducerAndPromotionDTO productAfterSaveOrder = getProductById(initProductTestData.getProductIdThatHasNoPromotion());

        assertNotEquals(productBeforeSaveOrder.quantity(), productAfterSaveOrder.quantity());

        assertsOrders(response, 1L, productAfterSaveOrder.price().multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(0.10))));
        assertEquals(0, discountRepository.findById(discount.getId()).get().getQuantity());
    }

    @Test
    void shouldSaveOrderWithTwoProducts_whenUserUseDiscount_forAuthenticatedUser_thenStatusCreated() {
        OrderRequest orderRequest = createOrderRequest(
                List.of(createShipmentRequest(initProductTestData.getProductIdThatHasNoPromotion()),
                        createShipmentRequest(initProductTestData.getProductIdThatHasOtherSubcategoryAndNoPromotion()),
                        createShipmentRequest(initProductTestData.getProductIdThatHasPromotion())),
                DeliveryBuilderHelper.createDeliveryRequest(deliveryTypeId),
                "test1"
        );

        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, getHttpHeaderWithUserToken());

        ResponseEntity<OrderResponse> response = sendPostRequest(httpEntity);

        BigDecimal priceOfProductThatHasNoPromotion = getProductById(
                initProductTestData.getProductIdThatHasNoPromotion()).price().multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(0.10)));
        BigDecimal priceOfProductThatHasOtherSubcategoryAndNoPromotion = getProductById(
                initProductTestData.getProductIdThatHasOtherSubcategoryAndNoPromotion()).price();
        BigDecimal priceOfProductThatHasPromotion = getProductById(initProductTestData.getProductIdThatHasPromotion()).promotionPrice();

        BigDecimal exceptedPrice = priceOfProductThatHasNoPromotion
                .add(priceOfProductThatHasOtherSubcategoryAndNoPromotion)
                .add(priceOfProductThatHasPromotion)
                .setScale(2, RoundingMode.HALF_UP);
        assertsOrders(response, 3L, exceptedPrice);
        assertEquals(0, discountRepository.findById(discount.getId()).get().getQuantity());
    }

    private Order creeteOrder(Account account, DeliveryType savedDeliveryType, Product product, LocalDateTime localDataTime) {
        BigDecimal productPrice = product.getPrice();
        String testAddress = "City: Test, Postcode: 99-999, Street: Test 59/2";
        return Order.builder()
                .account(account)
                .nameUser(account.getFirstName() + " " + account.getLastName())
                .shipments(List.of(Shipment.builder()
                        .quantity(1)
                        .product(product)
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

    private ProductWithProducerAndPromotionDTO getProductById(Long initProductTestData) {
        return productRepository.findById(
                initProductTestData,
                LocalDateTime.now().minusDays(30)
        );
    }

    private ResponseEntity<OrderResponse> sendPostRequest(HttpEntity<OrderRequest> httpEntity) {
        return restTemplate.exchange(
                localhostUri + ORDER_URI,
                HttpMethod.POST,
                httpEntity,
                OrderResponse.class
        );
    }

    private void assertsOrders(ResponseEntity<OrderResponse> response, Long exceptedShipmentSize, BigDecimal exceptedPrice) {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().id());
        assertNotNull(response.getBody().createdAt());
        assertNotNull(response.getBody().deliveryResponse());
        assertEquals(getSavedUser().getFirstName() + " " + getSavedUser().getLastName(),
                response.getBody().nameUser());
        assertEquals(OrderStatus.OPEN, response.getBody().status());
        assertEquals(exceptedShipmentSize, response.getBody().shipmentResponses().size());
        assertEquals(exceptedPrice.add(response.getBody().deliveryResponse().deliveryTypeResponse().price()).setScale(2, RoundingMode.HALF_UP),
                response.getBody().totalPrice()
        );
    }
}
