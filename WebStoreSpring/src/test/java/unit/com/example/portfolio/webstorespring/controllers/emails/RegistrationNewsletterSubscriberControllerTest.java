package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterNewsletterSubscriberService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationNewsletterSubscriberControllerTest {

    @Mock
    private RegisterNewsletterSubscriberService registerNewsletterSubscriberService;
    @InjectMocks
    private RegistrationNewsletterSubscriberController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/newsletter/registrations";

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldConfirm() throws Exception {
        ResponseMessageDTO response = new ResponseMessageDTO("Newsletter subscriber confirmed.");

        given(registerNewsletterSubscriberService.confirm(anyString())).willReturn(response);

        mvc.perform(get(URI + "/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }

    @Test
    void shouldRegistration() throws Exception {
        SubscriberRequest request = new SubscriberRequest("test@test.pl");
        ResponseMessageDTO response = new ResponseMessageDTO("Verify your email address using the link in your email.");

        given(registerNewsletterSubscriberService.register(any(SubscriberRequest.class))).willReturn(response);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());

    }
}