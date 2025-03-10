package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.services.unsubscribes.UnsubscribeProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UnsubscribeProductControllerTest {

    @Mock
    private UnsubscribeProductService unsubscribeProductService;
    @InjectMocks
    private UnsubscribeProductController underTest;

    private MockMvc mvc;

    private static final String URI = "/api/v1/unsubscribe-product-subscription";

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
    }

    @Test
    void unsubscribeAllSubscriptions() throws Exception {
        ResponseMessageDTO response = new ResponseMessageDTO("Your all subscription was removed");

        given(unsubscribeProductService.deleteAllSubscriptions(anyString())).willReturn(response);

        mvc.perform(delete(URI + "/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }

    @Test
    void unsubscribeSingleProduct() throws Exception {
        ResponseMessageDTO response = new ResponseMessageDTO("Your subscription for this: test was removed.");

        given(unsubscribeProductService.deleteFromSingleProduct(anyString())).willReturn(response);

        mvc.perform(delete(URI + "/single/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }
}