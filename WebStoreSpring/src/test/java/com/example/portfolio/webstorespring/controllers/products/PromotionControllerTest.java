package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequesst;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.services.products.PromotionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;

import static com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper.createPromotionResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {
    @Mock
    private PromotionService promotionService;
    @InjectMocks
    private PromotionController underTest;

    private static final String URI = "/api/v1/admin/products/promotions";
    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSavePromotion() throws Exception {
        PromotionRequesst promotionRequest = PromotionBuilderHelper.createPromotionRequest();
        PromotionResponse promotionResponse = createPromotionResponse();

        given(promotionService.savePromotion(any(PromotionRequesst.class)))
                .willReturn(promotionResponse);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(promotionResponse.getId().intValue())))
                .andExpect(jsonPath("$.promotionPrice", is(promotionResponse.getPromotionPrice().doubleValue())))
                .andExpect(jsonPath("$.startDate", is(promotionResponse.getStartDate().format(formatter))))
                .andExpect(jsonPath("$.endDate", is(promotionResponse.getEndDate().format(formatter))))
                .andDo(print());
    }
}
