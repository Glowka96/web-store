package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.controllers.accounts.AccountController;
import com.example.porfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
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
    private AccountDto accountDto;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("Test");
        accountDto.setLastName("Dev");
        accountDto.setPassword("Abcd123$");
        accountDto.setEmail("test@test.pl");
    }

    @Test
    void shouldGetAccountById() throws Exception {
        given(accountService.getAccountById(anyLong())).willReturn(accountDto);

        mvc.perform(get(URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountDto)))
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
        given(accountService.updateAccount(anyLong(), any(AccountDto.class))).willReturn(accountDto);

        mvc.perform(put(URL + "/{accountId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Dev")))
                .andExpect(jsonPath("$.email", is("test@test.pl")))
                .andDo(print());
    }

    @Test
    void shouldDeleteAccountById() throws Exception{
        mvc.perform(delete(URL + "/{accountId}", 1))
                .andExpect(status().isNoContent());
    }
}