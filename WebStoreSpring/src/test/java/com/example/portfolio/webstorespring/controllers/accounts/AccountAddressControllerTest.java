package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountAddressService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountAddressControllerTest {
    @Mock
    private AccountAddressService addressService;
    @InjectMocks
    private AccountAddressController underTest;

    private final static String URI = "/api/v1/accounts/address";
    private ObjectMapper mapper;
    private MockMvc mvc;
    private AccountAddressResponse addressResponse;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        addressResponse = AccountAddressResponse.builder()
                .id(1L)
                .city("Test")
                .street("test 59/2")
                .postcode("99-999")
                .build();
    }


    @Test
    void getAccountAddress() throws Exception {
        given(addressService.getAccountAddress()).willReturn(addressResponse);

        mvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addressResponse))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.street", is("test 59/2")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }

    @Test
    void saveAccountAddress() throws Exception {
        given(addressService.saveAccountAddress(any(AccountAddressRequest.class))).willReturn(addressResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addressResponse))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.street", is("test 59/2")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }

    @Test
    void updateAccountAddress() throws Exception {
        given(addressService.updateAccountAddress(any(AccountAddressRequest.class))).willReturn(addressResponse);

        mvc.perform(put(URI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addressResponse))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.street", is("test 59/2")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }
}
