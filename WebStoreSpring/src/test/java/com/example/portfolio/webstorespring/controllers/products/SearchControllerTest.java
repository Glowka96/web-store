package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.services.products.ProductsPageService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.PageProductWithPromotionDTOBuilderHelper.createPageProductsWithPromotionDTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @InjectMocks
    private SearchController underTest;
    @Mock
    private ProductsPageService productsPageService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URI = "/api/v1/products/search";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetSearchProductsByText() throws Exception {
        PageProductsWithPromotionDTO pageProducts = createPageProductsWithPromotionDTO();
        given(productsPageService.getPageSearchProducts(anyString(), anyInt(), anyInt(), any(), any()))
                .willReturn(pageProducts);

        mvc.perform(get(URI)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pageProducts)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
