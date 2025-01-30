package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.controllers.emails.ResetPasswordController;
import com.example.portfolio.webstorespring.model.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.services.emails.ResetPasswordService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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

    private ObjectMapper objectMapper;
    private MockMvc mvc;
    private static final String URI = "/api/v1";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldResetPassword() throws Exception {
        ResponseMessageDTO result = new ResponseMessageDTO("Sent reset password link to your email");

        given(resetPasswordService.sendResetPasswordLinkByEmail(anyString())).willReturn(result);

        mvc.perform(get(URI + "/reset-password")
                        .param("email", "test@test.pl")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(result.message())))
                .andDo(print());
    }

    @Test
    void shouldConfirmResetPassword() throws Exception {
        ResponseMessageDTO result = new ResponseMessageDTO("Your new password has been saved");
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123$");

        given(resetPasswordService.confirm(any(ResetPasswordRequest.class), anyString())).willReturn(result);

        mvc.perform(patch(URI + "/reset-password/confirm")
                        .param("token", "Token123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(result.message())))
                .andDo(print());
    }
}
