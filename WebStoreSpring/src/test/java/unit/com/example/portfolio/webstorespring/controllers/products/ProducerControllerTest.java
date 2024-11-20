package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.services.products.ProducerService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducerRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducerResponse;
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
class ProducerControllerTest {

    @Mock
    private ProducerService producerService;
    @InjectMocks
    private ProducerController underTest;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1/admin/producers";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllProducer() throws Exception {
        ProducerResponse producerResponse = createProducerResponse();

        given(producerService.getAll()).willReturn(List.of(producerResponse, producerResponse));

        mvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldSaveProducer() throws Exception {
        ProducerRequest productRequest = createProducerRequest();
        ProducerResponse producerResponse = createProducerResponse();

        given(producerService.save(any(ProducerRequest.class))).willReturn(producerResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(producerResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(producerResponse.name())))
                .andDo(print());
    }

    @Test
    void shouldUpdateProducer() throws Exception {
        ProducerRequest productRequest = createProducerRequest();
        ProducerResponse producerResponse = createProducerResponse();

        given(producerService.update(anyLong(), any(ProducerRequest.class))).willReturn(producerResponse);

        mvc.perform(put(URI + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(producerResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(producerResponse.name())))
                .andDo(print());
    }

    @Test
    void shouldDeleteProducer() throws Exception {
        mvc.perform(delete(URI + "/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
