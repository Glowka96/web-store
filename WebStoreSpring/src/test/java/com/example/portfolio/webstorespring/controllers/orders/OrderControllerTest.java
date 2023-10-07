package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.portfolio.webstorespring.services.orders.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({MockitoExtension.class})
class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1/accounts";
    private OrderResponse orderResponse;
    private OrderRequest orderRequest;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);

        ShipmentRequest shipmentRequest = new ShipmentRequest();
        shipmentRequest.setQuantity(3);
        shipmentRequest.setPrice(3.00);

        orderRequest = new OrderRequest();
        orderRequest.setDeliveryAddress("Test, 99-999, test 59/2");
        orderRequest.setShipmentRequests(new ArrayList<>(List.of(shipmentRequest)));
    }


    @Test
    void shouldGetAllOrders() throws Exception {
        List<OrderResponse> orderResponses = new ArrayList<>(Arrays.asList(orderResponse, new OrderResponse()));
        given(orderService.getAllAccountOrder()).willReturn(orderResponses);

        mvc.perform(get(URI + "/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetOrderByAccountIdAndId() throws Exception {
        given(orderService.getAccountOrderByOrderId(anyLong())).willReturn(orderResponse);

        mvc.perform(get(URI + "/orders/{id}",  1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldSaveOrder() throws Exception {
        given(orderService.saveOrder(any(OrderRequest.class))).willReturn(orderResponse);

        mvc.perform(post(URI + "/orders", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldUpdateOrder() throws Exception {
        given(orderService.updateOrder(anyLong(), any(OrderRequest.class))).willReturn(orderResponse);

        mvc.perform(put(URI + "/orders/{ordersId}",  1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldDeleteOrderById() throws Exception {
        mvc.perform(delete(URI + "/orders/{orderId}",  1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
