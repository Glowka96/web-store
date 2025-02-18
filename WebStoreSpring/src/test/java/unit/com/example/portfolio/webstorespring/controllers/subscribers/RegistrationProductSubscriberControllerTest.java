package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterProductSubscriberService;
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
class RegistrationProductSubscriberControllerTest {

    @Mock
    private RegisterProductSubscriberService registerProductSubscriberService;
    @InjectMocks
    private RegistrationProductSubscriberController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;

    private static final String URI = "/api/v1/product-subscription/registrations";

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldConfirm() throws Exception {
        ResponseMessageDTO response = new ResponseMessageDTO("Product subscriber confirmed.");

        given(registerProductSubscriberService.confirm(anyString())).willReturn(response);

        mvc.perform(get(URI + "/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }

    @Test
    void shouldRegistration() throws Exception {
        SubscriberRequest subscriberRequest = new SubscriberRequest("test@test.pl");
        ProductSubscriberRequest request = new ProductSubscriberRequest(subscriberRequest, 1L);
        ResponseMessageDTO response = new ResponseMessageDTO("Verify your email address using the link in your email.");

        given(registerProductSubscriberService.register(any(ProductSubscriberRequest.class))).willReturn(response);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());

    }
}