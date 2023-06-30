package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.AuthenticationResponse;
import com.example.portfolio.webstorespring.model.dto.accounts.LoginRequest;
import com.example.portfolio.webstorespring.services.accounts.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginController underTest;
    private final static String URL = "/api/v1/login";

    @Test
    void login() throws Exception {
        // given
        MockMvc mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.pl");
        loginRequest.setPassword("Test1234$");

        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");

        // when
        when(loginService.login(loginRequest)).thenReturn(authenticationResponse);

        // then
        mvc.perform(post(URL, loginRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token")))
                .andDo(print());
    }
}
