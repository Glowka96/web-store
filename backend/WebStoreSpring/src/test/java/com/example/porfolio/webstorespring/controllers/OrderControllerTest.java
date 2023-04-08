package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.controllers.orders.OrderController;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.services.orders.OrderService;
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
    private static final String URL = "/api/v1/accounts/{accountId}";
    private OrderDto orderDto;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        orderDto = new OrderDto();
        orderDto.setId(1L);
    }


    @Test
    void shouldGetAllOrders() throws Exception {
        List<OrderDto> orderDtos = new ArrayList<>(Arrays.asList(orderDto, new OrderDto()));
        given(orderService.getAllOrderDtoByAccountId(anyLong())).willReturn(orderDtos);

        mvc.perform(get(URL + "/orders", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetOrderByAccountIdAndId() throws Exception {
        given(orderService.getOrderByAccountIdAndOrderId(anyLong(), anyLong())).willReturn(orderDto);

        mvc.perform(get(URL + "/orders/{id}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldSaveOrder() throws Exception {
        given(orderService.saveOrder(1L, orderDto)).willReturn(orderDto);

        mvc.perform(post(URL + "/orders", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderDto))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldUpdateOrder() throws Exception {
        given(orderService.updateOrder(anyLong(), anyLong(), any(OrderDto.class))).willReturn(orderDto);

        mvc.perform(put(URL + "/orders/{ordersId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderDto))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldDeleteOrderById() throws Exception {
        mvc.perform(delete(URL + "/orders/{orderId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}