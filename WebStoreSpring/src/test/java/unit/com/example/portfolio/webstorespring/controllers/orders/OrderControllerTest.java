package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.controllers.AccountDetailsArgumentResolver;
import com.example.portfolio.webstorespring.models.dtos.orders.requests.OrderRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.OrderResponse;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
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

import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.orders.OrderBuilderHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setCustomArgumentResolvers(new AccountDetailsArgumentResolver())
                .build();

        mapper = new ObjectMapper();
    }


    @Test
    void shouldGetAllOrders() throws Exception {
        OrderResponse orderResponse = createOrderResponseWithoutShipments();
        given(orderService.getAllAccountOrder(any(AccountDetails.class)))
                .willReturn(List.of(orderResponse, orderResponse));

        mvc.perform(get(URI + "/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetAccountOrderByOrderId() throws Exception {
        OrderResponse orderResponse = createOrderResponse();
        given(orderService.getById(any(AccountDetails.class), anyLong()))
                .willReturn(orderResponse);

        mvc.perform(get(URI + "/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldGetLastFiveAccountOrders() throws Exception {
        OrderResponse orderResponse = createOrderResponseWithoutShipments();

        given(orderService.getLastFiveAccountOrder(any(AccountDetails.class)))
                .willReturn(List.of(orderResponse, orderResponse, orderResponse, orderResponse, orderResponse));

        mvc.perform(get(URI + "/orders/last-five")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andDo(print());
    }

    @Test
    void shouldSaveOrder() throws Exception {
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse orderResponse = createOrderResponse();

        given(orderService.save(any(AccountDetails.class), any(OrderRequest.class)))
                .willReturn(orderResponse);

        mvc.perform(post(URI + "/orders", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }
}
