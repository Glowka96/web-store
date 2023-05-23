package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.services.products.ProductService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
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
    private ProductService productService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URL = "/api/v1/products/search";
    private List<ProductRequest> productRequestList;

   @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(1L);
        productRequest.setName("Test");
        productRequest.setPrice(20.0);
        productRequest.setDescription("Description test");
        ProductRequest productRequest1 = new ProductRequest();
        productRequest1.setId(2L);
        productRequest1.setName("Test2");
        productRequest1.setPrice(21.0);
        productRequest1.setDescription("Description test2");
        ProductRequest productRequest2 = new ProductRequest();
        productRequest2.setId(3L);
        productRequest2.setName("Test3");
        productRequest2.setPrice(22.0);
        productRequest2.setDescription("Description test3");
        productRequestList = new ArrayList<>(Arrays.asList(productRequest, productRequest1, productRequest2));
    }

    @Test
    void shouldGetSearchProductsByText() throws Exception {
        given(productService.getSearchProducts(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(productRequestList);

        mvc.perform(get(URL + "/{search}", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequestList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldGetAmountSearchProducts() throws Exception {
        given(productService.getAmountSearchProducts(anyString())).willReturn(12L);

        mvc.perform(get(URL + "/{search}/amount", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequestList)))
                .andExpect(jsonPath("$", is(12)))
                .andDo(print());
    }
}