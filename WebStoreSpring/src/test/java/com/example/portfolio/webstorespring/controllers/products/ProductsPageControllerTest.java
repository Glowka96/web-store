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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        PageProductsWithPromotionDTO pageProducts = createPageProductsWithPromotionDTO();

        given(productsPageService.getPageProductsBySubcategoryId(anyLong(), anyInt(), anyInt(), any(), any()))
                .willReturn(pageProducts);

        mvc.perform(get(URI + "/subcategories/{subcategoryId}/products", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(pageProducts.totalElements().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageProducts.totalPages())))
                .andExpect(jsonPath("$.sortByTypes", hasSize(pageProducts.sortByTypes().size())))
                .andExpect(jsonPath("$.sortDirectionTypes", hasSize(pageProducts.sortDirectionTypes().size())))
                .andExpect(jsonPath("$.products", hasSize(pageProducts.products().size())))
                .andDo(print());
    }

    @Test
    void getPagePromotionProduct() throws Exception {
        PageProductsWithPromotionDTO pageProducts = createPageProductsWithPromotionDTO();

        given(productsPageService.getPagePromotionProduct(anyInt(), anyInt(), any(), any()))
                .willReturn(pageProducts);

        mvc.perform(get(URI + "/products/promotions", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(pageProducts.totalElements().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageProducts.totalPages())))
                .andExpect(jsonPath("$.sortByTypes", hasSize(pageProducts.sortByTypes().size())))
                .andExpect(jsonPath("$.sortDirectionTypes", hasSize(pageProducts.sortDirectionTypes().size())))
                .andExpect(jsonPath("$.products", hasSize(pageProducts.products().size())))
                .andDo(print());
    }

    @Test
    void getPageNewProduct() throws Exception {
        PageProductsWithPromotionDTO pageProducts = createPageProductsWithPromotionDTO();

        given(productsPageService.getPageNewProduct(anyInt(), anyInt(), any(), any()))
                .willReturn(pageProducts);

        mvc.perform(get(URI + "/products/news", 1)
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "PRICE")
                        .param("direction", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(pageProducts.totalElements().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageProducts.totalPages())))
                .andExpect(jsonPath("$.sortByTypes", hasSize(pageProducts.sortByTypes().size())))
                .andExpect(jsonPath("$.sortDirectionTypes", hasSize(pageProducts.sortDirectionTypes().size())))
                .andExpect(jsonPath("$.products", hasSize(pageProducts.products().size())))
                .andDo(print());
    }
}
