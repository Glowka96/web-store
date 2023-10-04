package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
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
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URI = "/api/v1/accounts";
    private AccountResponse accountResponse;
    private AccountRequest accountRequest;

    @BeforeEach
    void initialization() {
        mapper = new ObjectMapper();

        accountResponse = AccountResponse.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Dev")
                .email("test@test.pl")
                .build();

        accountRequest = AccountRequest.builder()
                .firstName("Test")
                .lastName("Dev")
                .password("Abcd123$")
                .imageUrl("https://i.imgur.com/a23SANX.png")
                .build();

        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldGetAccountById() throws Exception {
        given(accountService.getAccount()).willReturn(accountResponse);

        mvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Dev")))
                .andExpect(jsonPath("$.email", is("test@test.pl")))
                .andDo(print());
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        given(accountService.updateAccount(any(AccountRequest.class))).willReturn(accountResponse);

        mvc.perform(put(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Dev")))
                .andExpect(jsonPath("$.email", is("test@test.pl")))
                .andDo(print());
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mvc.perform(delete(URI))
                .andExpect(status().isNoContent());
    }
}
