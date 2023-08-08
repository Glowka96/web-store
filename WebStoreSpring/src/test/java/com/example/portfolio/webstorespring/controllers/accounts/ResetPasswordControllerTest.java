package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.AccountPasswordRequest;
import com.example.portfolio.webstorespring.services.accounts.ResetPasswordService;
import com.example.portfolio.webstorespring.services.email.type.ResetPasswordType;
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

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ResetPasswordControllerTest {
    @Mock
    private ResetPasswordService resetPasswordService;
    @InjectMocks
    private ResetPasswordController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URL = "/api/v1/accounts";
    private Map<String,Object> result;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();
        mapper = new ObjectMapper();
    }
    @Test
    void shouldResetPassword() throws Exception {
        result = Map.of("message", ResetPasswordType.PASSWORD.getMessage());

        given(resetPasswordService.resetPasswordByEmail(anyString())).willReturn(result);

        mvc.perform(get(URL + "/{email}/reset-password", "test@test.pl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(result)))
                .andDo(print());
    }

    @Test
    void shouldConfirmResetPassword() throws Exception {
        result = Map.of("message", "Your new password has been saved");
        AccountPasswordRequest accountPasswordRequest = new AccountPasswordRequest();
        accountPasswordRequest.setPassword("Test123*");

        given(resetPasswordService.confirmResetPassword(anyString(), anyString())).willReturn(result);

        mvc.perform(patch(URL + "/reset-password/confirm")
                .param("token", "token123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountPasswordRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", is(result)))
                .andDo(print());
    }
}
