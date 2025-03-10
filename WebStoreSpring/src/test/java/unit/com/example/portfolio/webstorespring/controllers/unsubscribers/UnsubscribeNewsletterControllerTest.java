package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.services.unsubscribes.UnsubscribeNewsletterService;
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
class UnsubscribeNewsletterControllerTest {

    @Mock
    private UnsubscribeNewsletterService unsubscribeNewsletterService;
    @InjectMocks
    private UnsubscribeNewsletterController underTest;

    @Test
    void shouldUnsubscribe() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        ResponseMessageDTO response = new ResponseMessageDTO("Your subscription was removed");

        given(unsubscribeNewsletterService.deleteSubscription(anyString())).willReturn(response);

        mvc.perform(delete("/api/v1/unsubscribe-newsletter/confirm")
                        .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }
}