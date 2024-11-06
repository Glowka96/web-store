package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AccountDetailsArgumentResolver;
import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountRequest;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountResponse;
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
    private static final String URI = "/api/v1/accounts";

    @BeforeEach
    void initialization() {
        mapper = new ObjectMapper();

        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new AccountDetailsArgumentResolver())
                .build();
    }

    @Test
    void shouldGetAccountById() throws Exception {
        AccountResponse accountResponse = createAccountResponse();

        given(accountService.getAccount(any(AccountDetails.class))).willReturn(accountResponse);

        mvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(accountResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(accountResponse.getLastName())))
                .andExpect(jsonPath("$.email", is(accountResponse.getEmail())))
                .andDo(print());
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        AccountRequest accountRequest = createAccountRequest();
        AccountResponse accountResponse = createAccountResponse();

        given(accountService.updateAccount(any(AccountDetails.class), any(AccountRequest.class)))
                .willReturn(accountResponse);

        mvc.perform(put(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(accountResponse.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(accountResponse.getLastName())))
                .andExpect(jsonPath("$.email", is(accountResponse.getEmail())))
                .andDo(print());
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mvc.perform(delete(URI))
                .andExpect(status().isNoContent());
    }
}
