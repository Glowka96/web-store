package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterMessageService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NewsletterMessageControllerTest {

    @Mock
    private NewsletterMessageService newsletterMessageService;
    @InjectMocks
    private NewsletterMessageController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1/admin/newsletters/messages";

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldSave() throws Exception {
        NewsletterMessageRequest request = new NewsletterMessageRequest("Test message", null);
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO("Newsletter message send to enabled subscribers");

        given(newsletterMessageService.save(any(NewsletterMessageRequest.class))).willReturn(responseMessageDTO);

        mvc.perform(post(URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(responseMessageDTO.message())))
                .andDo(print());
    }
}