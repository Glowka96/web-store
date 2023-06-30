package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProducerResponse;
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
class ProducerControllerTest {

    @Mock
    private ProducerService producerService;
    @InjectMocks
    private ProducerController underTest;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URL = "/api/v1/producers";
    private ProducerResponse producerResponse;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        producerResponse = new ProducerResponse();
        producerResponse.setId(1L);
        producerResponse.setName("Test");
    }

    @Test
    void shouldGetAllProducer() throws Exception {
        given(producerService.getAllProducer()).willReturn(Arrays.asList(producerResponse, new ProducerResponse()));

        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetProducerById() throws Exception {
        given(producerService.getProducerById(anyLong())).willReturn(producerResponse);

        mvc.perform(get(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldSaveProducer() throws Exception {
        given(producerService.save(any(ProducerRequest.class))).willReturn(producerResponse);

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producerResponse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldUpdateProducer() throws Exception {
        given(producerService.update(anyLong(), any(ProducerRequest.class))).willReturn(producerResponse);

        mvc.perform(put(URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producerResponse)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldDeleteProducer() throws Exception{
        mvc.perform(delete(URL + "/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
