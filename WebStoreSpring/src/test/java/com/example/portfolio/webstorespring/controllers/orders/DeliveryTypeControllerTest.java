package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.services.orders.DeliveryTypeService;
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

import static com.example.portfolio.webstorespring.buildhelpers.DeliveryTypeBuilderHelper.createDeliveryTypeRequest;
import static com.example.portfolio.webstorespring.buildhelpers.DeliveryTypeBuilderHelper.createDeliveryTypeResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class DeliveryTypeControllerTest {

    @Mock
    private DeliveryTypeService deliveryTypeService;
    @InjectMocks
    private DeliveryTypeController underTest;

    private static final String URI = "/api/v1/delivery-type";
    private MockMvc mvc;
    private ObjectMapper mapper;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }
    @Test
    void shouldGetAllDeliveryType() throws Exception {
        DeliveryTypeResponse deliveryTypeResponse = createDeliveryTypeResponse();
        given(deliveryTypeService.getAllDeliveryType()).willReturn(List.of(deliveryTypeResponse, deliveryTypeResponse));

        mvc.perform(get(URI + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldSaveDelivery() throws Exception {
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest();
        DeliveryTypeResponse deliveryTypeResponse = createDeliveryTypeResponse();

        given(deliveryTypeService.saveDeliveryType(any(DeliveryTypeRequest.class))).willReturn(deliveryTypeResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deliveryTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is(deliveryTypeRequest.getType())))
                .andExpect(jsonPath("$.price", is(10.0)))
                .andDo(print());
    }

    @Test
    void deleteDeliveryTypeById() throws Exception {
        mvc.perform(delete(URI + "/{deliveryId}", 1))
                .andExpect(status().isNoContent());
    }
}
