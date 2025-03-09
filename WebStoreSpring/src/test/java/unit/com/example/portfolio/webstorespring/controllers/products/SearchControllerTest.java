package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductsPageOptionsBuilderHelper;
import com.example.portfolio.webstorespring.models.dtos.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductsPageOptions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @InjectMocks
    private SearchController underTest;
    @Mock
    private ProductsPageService productsPageService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1/products/search";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetSearchProductsByText() throws Exception {
        PageProductsWithPromotionDTO pageProducts = createPageProductsWithPromotionDTO();

        given(productsPageService.getSearchProductsPage(anyString(), any(ProductsPageOptions.class)))
                .willReturn(pageProducts);

        mvc.perform(get(URI)
                        .param("query", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ProductsPageOptionsBuilderHelper.createBaseProductPageOptions())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(pageProducts.totalElements().intValue())))
                .andExpect(jsonPath("$.totalPages", is(pageProducts.totalPages())))
                .andExpect(jsonPath("$.sortOptions", hasSize(pageProducts.sortOptions().size())))
                .andExpect(jsonPath("$.products", hasSize(pageProducts.products().size())))
                .andDo(print());
    }
}
