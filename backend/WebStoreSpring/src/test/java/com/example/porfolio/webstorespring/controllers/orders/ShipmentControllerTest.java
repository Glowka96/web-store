package com.example.porfolio.webstorespring.controllers.orders;

import com.example.porfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.dto.products.ProductResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private ShipmentResponse shipmentResponse;
    private ShipmentRequest shipmentRequest;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setPrice(20.0);
        productRequest.setName("Test");
        productRequest.setDescription("Test description");
        productRequest.setImageUrl("https://www.trefl.com/media/catalog/product/cache/550c1e1c568f7ff4e3f4d09dfa9b2306/3/7/37459_150_01.png");

        shipmentRequest = new ShipmentRequest();
        shipmentRequest.setProductRequest(productRequest);
        shipmentRequest.setQuantity(3);
        shipmentRequest.setPrice(60.0);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setPrice(20.0);
        productResponse.setName("Test");
        productResponse.setDescription("Test description");
        productResponse.setImageUrl("https://www.trefl.com/media/catalog/product/cache/550c1e1c568f7ff4e3f4d09dfa9b2306/3/7/37459_150_01.png");

        shipmentResponse = new ShipmentResponse();
        shipmentResponse.setId(1L);
        shipmentResponse.setQuantity(3);
        shipmentResponse.setProductResponse(productResponse);
    }

    @Test
    void shouldGetAllShipments() throws Exception {
        // given
        given(shipmentService.getAllShipment()).willReturn(Arrays.asList(shipmentResponse, shipmentResponse));

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
        given(shipmentService.getShipmentDtoById(anyLong())).willReturn(shipmentResponse);

        // when
        // then
        mvc.perform(get(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldSaveShipment() throws Exception {
        // given
        given(shipmentService.save(any(ShipmentRequest.class))).willReturn(shipmentResponse);

        // when
        // then
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(3)))
                .andDo(print());
    }

    @Test
    void shouldUpdateShipment() throws Exception {
        // given
        given(shipmentService.update(anyLong(), any(ShipmentRequest.class))).willReturn(shipmentResponse);

        // when
        // then
        mvc.perform(put(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(shipmentRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(3)))
                .andDo(print());
    }

    @Test
    void shouldDeleteShipmentById() throws Exception {
        mvc.perform(delete(URL + "/{id}" , 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}