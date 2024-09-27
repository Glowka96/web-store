package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AccountDetailsArgumentResolver;
import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountAddressService;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressResponse;
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

    private final static String URI = "/api/v1/accounts/addresses";
    private ObjectMapper mapper;
    private MockMvc mvc;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new AccountDetailsArgumentResolver())
                .build();

        mapper = new ObjectMapper();
    }


    @Test
    void getAccountAddress() throws Exception {
        AccountAddressResponse accountAddressResponse = createAccountAddressResponse();

        given(addressService.getAccountAddress(any(AccountDetails.class)))
                .willReturn(accountAddressResponse);

        mvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is(accountAddressResponse.getCity())))
                .andExpect(jsonPath("$.street", is(accountAddressResponse.getStreet())))
                .andExpect(jsonPath("$.postcode", is(accountAddressResponse.getPostcode())))
                .andDo(print());
    }

    @Test
    void saveAccountAddress() throws Exception {
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddressResponse accountAddressResponse = createAccountAddressResponse();

        given(addressService.saveAccountAddress(any(AccountDetails.class), any(AccountAddressRequest.class)))
                .willReturn(accountAddressResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountAddressRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is(accountAddressResponse.getCity())))
                .andExpect(jsonPath("$.street", is(accountAddressResponse.getStreet())))
                .andExpect(jsonPath("$.postcode", is(accountAddressResponse.getPostcode())))
                .andDo(print());
    }

    @Test
    void updateAccountAddress() throws Exception {
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddressResponse accountAddressResponse = createAccountAddressResponse();

        given(addressService.updateAccountAddress(any(AccountDetails.class), any(AccountAddressRequest.class)))
                .willReturn(accountAddressResponse);

        mvc.perform(put(URI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountAddressRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is(accountAddressResponse.getCity())))
                .andExpect(jsonPath("$.street", is(accountAddressResponse.getStreet())))
                .andExpect(jsonPath("$.postcode", is(accountAddressResponse.getPostcode())))
                .andDo(print());
    }
}
