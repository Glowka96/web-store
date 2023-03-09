package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.services.AccountAddressService;
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
import static org.mockito.ArgumentMatchers.anyLong;
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

    private final static String URL = "/api/v1/accounts/{accountId}/address";
    private ObjectMapper mapper;
    private MockMvc mvc;
    private AccountAddressDto address;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        AccountDto account = new AccountDto();
        account.setId(1L);

        address = new AccountAddressDto();
        address.setId(1L);
        address.setCity("Test");
        address.setAddress("test 59");
        address.setPostcode("99-999");
        address.setAccountDto(account);

    }


    @Test
    void getAccountAddressByAccountId() throws Exception {
        given(addressService.getAccountAddressByAccountId(anyLong())).willReturn(address);

        mvc.perform(get(URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.address", is("test 59")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }

    @Test
    void saveAccountAddress() throws Exception {
        given(addressService.saveAccountAddress(anyLong(), any(AccountAddressDto.class))).willReturn(address);

        mvc.perform(post(URL, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.address", is("test 59")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }

    @Test
    void updateAccountAddress() throws Exception {
        given(addressService.updateAccountAddress(anyLong(), any(AccountAddressDto.class))).willReturn(address);

        mvc.perform(put(URL, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.address", is("test 59")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }
}