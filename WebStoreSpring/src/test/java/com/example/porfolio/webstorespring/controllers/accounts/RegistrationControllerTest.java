package com.example.porfolio.webstorespring.controllers.accounts;

import com.example.porfolio.webstorespring.services.accounts.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private Map<String, Object> result;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();

        mapper = new ObjectMapper();
    }

    @Test
    void shouldConfirm() throws Exception {
        result = new HashMap<>();
        result.put("message", "Account confirmed");
        given(registrationService.confirmToken(anyString())).willReturn(result);

        mvc.perform(get("/api/v1/registration/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$",is(result)));

    }

//    //TODO Repair HV000064: Unable to instantiate ConstraintValidator:
//    @Test
//    void shouldRegistration() throws Exception {
//        RegistrationRequest request = new RegistrationRequest();
//        request.setFirstName("Test");
//        request.setLastName("Test");
//        request.setEmail("test@test.pl");
//        request.setPassword("password");
//
//        result = new HashMap<>();
//        result.put("message","Verify email by the link sent on your email address");
//
//        given(registrationService.registrationAccount(any(RegistrationRequest.class)))
//                .willReturn(result);
//
//        mvc.perform(post(URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$",
//                        is("Verify email by the link sent on your email address")));
//    }
}
