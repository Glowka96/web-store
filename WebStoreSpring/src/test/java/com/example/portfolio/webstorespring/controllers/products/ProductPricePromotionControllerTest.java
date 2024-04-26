package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.services.products.ProductPricePromotionService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.createProductPricePromotionRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.createProductPricePromotionResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductPricePromotionControllerTest {
    @Mock
    private ProductPricePromotionService promotionService;
    @InjectMocks
    private ProductPricePromotionController underTest;

    private static final String URI = "/api/v1/admin/products/promotions";
    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldSaveProductPricePromotion() throws Exception {
        ProductPricePromotionRequest promotionRequest = createProductPricePromotionRequest();
        ProductPricePromotionResponse promotionResponse = createProductPricePromotionResponse();

        given(promotionService.saveProductPricePromotion(any(ProductPricePromotionRequest.class)))
                .willReturn(promotionResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(promotionResponse.getId().intValue())))
                .andExpect(jsonPath("$.promotionPrice", is(promotionResponse.getPromotionPrice().doubleValue())))
                .andExpect(jsonPath("$.startDate", is(promotionResponse.getStartDate().getTime())))
                .andExpect(jsonPath("$.endDate", is(promotionResponse.getEndDate().getTime())))
                .andDo(print());
    }
}
