package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.AccountDetailsArgumentResolver;
import com.example.portfolio.webstorespring.exceptions.GlobalExceptionHandler;
import com.example.portfolio.webstorespring.model.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.LoginRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdateEmailRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdatePasswordRequest;
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

        given(accountService.getByAccountDetails(any(AccountDetails.class))).willReturn(accountResponse);

        mvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(accountResponse.firstName())))
                .andExpect(jsonPath("$.lastName", is(accountResponse.lastName())))
                .andExpect(jsonPath("$.email", is(accountResponse.email())))
                .andDo(print());
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        AccountRequest accountRequest = createAccountRequest();
        AccountResponse accountResponse = createAccountResponse();

        given(accountService.update(any(AccountDetails.class), any(AccountRequest.class)))
                .willReturn(accountResponse);

        mvc.perform(put(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(accountResponse.firstName())))
                .andExpect(jsonPath("$.lastName", is(accountResponse.lastName())))
                .andExpect(jsonPath("$.email", is(accountResponse.email())))
                .andDo(print());
    }

    @Test
    void shouldUpdateEmail() throws Exception {
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest(
                "newEmail@test.pl",
                new LoginRequest("oldEmail@test.pl", "Password123*")
        );
        String msg = "Email updated successfully.";
        ResponseMessageDTO message = new ResponseMessageDTO(msg);

        given(accountService.updateEmail(any(AccountDetails.class), any(UpdateEmailRequest.class)))
                .willReturn(message);

        mvc.perform(patch(URI + "/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateEmailRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(msg)))
                .andDo(print());
    }

    @Test
    void shouldUpdatePassword() throws Exception {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("CurrentlyPassword1*", "NewPassword1*");

        String msg = "Password updated successfully.";
        ResponseMessageDTO message = new ResponseMessageDTO(msg);


        given(accountService.updatePassword(any(AccountDetails.class), any(UpdatePasswordRequest.class)))
                .willReturn(message);

        mvc.perform(patch(URI + "/passwords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePasswordRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(msg)))
                .andDo(print());
    }

    @Test
    void shouldDeleteAccount() throws Exception {
        mvc.perform(delete(URI))
                .andExpect(status().isNoContent());
    }
}
