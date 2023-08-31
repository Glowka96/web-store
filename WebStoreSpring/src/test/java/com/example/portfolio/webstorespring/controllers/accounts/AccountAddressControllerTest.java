package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountResponse;
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

    private final static String URI = "/api/v1/accounts/{accountId}/address";
    private ObjectMapper mapper;
    private MockMvc mvc;
    private AccountAddressResponse address;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        AccountResponse account = new AccountResponse();
        account.setId(1L);

        address = new AccountAddressResponse();
        address.setId(1L);
        address.setCity("Test");
        address.setStreet("test 59/2");
        address.setPostcode("99-999");
    }


    @Test
    void getAccountAddressByAccountId() throws Exception {
        given(addressService.getAccountAddressByAccountId(anyLong())).willReturn(address);

        mvc.perform(get(URI, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address))
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
        given(addressService.saveAccountAddress(anyLong(), any(AccountAddressRequest.class))).willReturn(address);

        mvc.perform(post(URI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address))
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
        given(addressService.updateAccountAddress(anyLong(), any(AccountAddressRequest.class))).willReturn(address);

        mvc.perform(put(URI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Test")))
                .andExpect(jsonPath("$.street", is("test 59/2")))
                .andExpect(jsonPath("$.postcode", is("99-999")))
                .andDo(print());
    }
}
