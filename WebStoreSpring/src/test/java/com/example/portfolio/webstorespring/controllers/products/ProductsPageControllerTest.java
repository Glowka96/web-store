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
class ProductsPageControllerTest {

    @Mock
    private ProductsPageService productsPageService;
    @InjectMocks
    private ProductsPageController underTest;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URI = "/api/v1";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetPageProductsBySubcategoryId() throws Exception {
        PageProductsWithPromotionDTO pageProductsWithPromotionDTO = createPageProductsWithPromotionDTO();

        given(productsPageService.getPageProductsBySubcategoryId(anyLong(), anyInt(), anyInt(), any(), any()))
                .willReturn(pageProductsWithPromotionDTO);

        mvc.perform(get(URI + "/subcategories/{subcategoryId}/products", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPagePromotionProduct() throws Exception {
        PageProductsWithPromotionDTO pageProductsWithPromotionDTO = createPageProductsWithPromotionDTO();

        given(productsPageService.getPagePromotionProduct(anyInt(), anyInt(), any(), any()))
                .willReturn(pageProductsWithPromotionDTO);

        mvc.perform(get(URI + "/promotions/products", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPageNewProduct() throws Exception {
        PageProductsWithPromotionDTO pageProductsWithPromotionDTO = createPageProductsWithPromotionDTO();

        given(productsPageService.getPageNewProduct(anyInt(), anyInt(), any(), any()))
                .willReturn(pageProductsWithPromotionDTO);

        mvc.perform(get(URI + "/new-products", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
