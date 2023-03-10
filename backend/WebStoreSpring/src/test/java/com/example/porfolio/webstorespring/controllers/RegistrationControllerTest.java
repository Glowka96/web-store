package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.porfolio.webstorespring.services.RegistrationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private RegistrationController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URL = "/api/v1/registration";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();

        mapper = new ObjectMapper();
    }

    @Test
    void shouldConfirm() throws Exception {
        String result = "Account confirmed";
        given(registrationService.confirmToken(anyString())).willReturn(result);

        mvc.perform(get("/api/v1/registration/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$",is("Account confirmed")));

    }

    //TODO Repair HV000064: Unable to instantiate ConstraintValidator:
    @Test
    void shouldRegistration() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setFirstName("Test");
        request.setLastName("Test");
        request.setEmail("test@test.pl");
        request.setPassword("password");

        given(registrationService.registrationAccount(any(RegistrationRequest.class)))
                .willReturn("Verify email by the link sent on your email address");

        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",
                        is("Verify email by the link sent on your email address")));
    }
}