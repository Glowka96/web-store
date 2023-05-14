package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.controllers.accounts.AccountController;
import com.example.porfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.porfolio.webstorespring.services.accounts.AccountService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URL = "/api/v1/accounts";
    private AccountResponse accountResponse;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        accountResponse = new AccountResponse();
        accountResponse.setId(1L);
        accountResponse.setFirstName("Test");
        accountResponse.setLastName("Dev");
        accountResponse.setEmail("test@test.pl");
    }

    @Test
    void shouldGetAccountById() throws Exception {
        when(accountService.getAccountById(anyLong())).thenReturn(accountResponse);

        mvc.perform(get(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountResponse))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Dev")))
                .andExpect(jsonPath("$.email", is("test@test.pl")))
                .andDo(print());
    }

    /*TODO
     *  repair ValidationException: HV000064: Unable to instantiate ConstraintValidator
     * */
    @Test
    void shouldUpdateAccount() throws Exception {
        given(accountService.updateAccount(anyLong(), any(AccountRequest.class))).willReturn(accountResponse);

        mvc.perform(put(URL + "/{accountId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountResponse))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Dev")))
                .andExpect(jsonPath("$.email", is("test@test.pl")))
                .andDo(print());
    }

    @Test
    void shouldDeleteAccountById() throws Exception {
        mvc.perform(delete(URL + "/{accountId}", 1))
                .andExpect(status().isNoContent());
    }
}