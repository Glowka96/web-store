package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.controllers.orders.ShipmentController;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.services.orders.ShipmentService;
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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
class ShipmentControllerTest {
    @InjectMocks
    private ShipmentController underTest;
    @Mock
    private ShipmentService shipmentService;

    private ObjectMapper mapper;
    private MockMvc mvc;

    private final static String URL = "/api/v1/shipments";
    private ShipmentDto shipmentDto;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        ProductDto  productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setPrice(20.0);

        shipmentDto = new ShipmentDto();
        shipmentDto.setId(1L);
        shipmentDto.setQuality(3);
        shipmentDto.setProductDto(productDto);
    }

    @Test
    void shouldGetAllShipments() throws Exception {
        // given
        given(shipmentService.getAllShipment()).willReturn(Arrays.asList(shipmentDto, shipmentDto));

        // when
        // then
        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetShipmentById() throws Exception {
        // given
        given(shipmentService.getShipmentDtoById(1L)).willReturn(shipmentDto);

        // when
        // then
        mvc.perform(get(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldSaveShipment() throws Exception {
        // given
        given(shipmentService.save(shipmentDto)).willReturn(shipmentDto);

        // when
        // then
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quality", is(3)))
                .andDo(print());
    }

    @Test
    void shouldUpdateShipment() throws Exception {
        // given
        given(shipmentService.update(1L, shipmentDto)).willReturn(shipmentDto);

        // when
        // then
        mvc.perform(put(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quality", is(3)))
                .andDo(print());
    }

    @Test
    void shouldDeleteShipmentById() throws Exception {
        mvc.perform(delete(URL + "/{id}" , 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}