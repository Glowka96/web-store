package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
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
    private List<ProductDto> productDtoList;

   @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test");
        productDto.setPrice(20.0);
        productDto.setDescription("Description test");
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(2L);
        productDto1.setName("Test2");
        productDto1.setPrice(21.0);
        productDto1.setDescription("Description test2");
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(3L);
        productDto2.setName("Test3");
        productDto2.setPrice(22.0);
        productDto2.setDescription("Description test3");
        productDtoList = new ArrayList<>(Arrays.asList(productDto, productDto1, productDto2));
    }

    @Test
    void shouldGetSearchProductsByText() throws Exception {
        given(productService.getSearchProducts(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(productDtoList);

        mvc.perform(get(URL + "/{search}", "test")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDtoList)))
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
                        .content(mapper.writeValueAsString(productDtoList)))
                .andExpect(jsonPath("$", is(12)))
                .andDo(print());
    }
}