package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.controllers.products.ProductController;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class})
class ProductControllerTest {

    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URL = "/api/v1/subcategories";
    private ProductDto productDto;
    private List<ProductDto> productDtoList;


    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        productDto = new ProductDto();
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
    void shouldGetAllProductsBySubCategoryIdPaginationNoSort() throws Exception {
        given(productService.getAllProductsBySubcategoryId(1L, 0, 3))
                .willReturn(productDtoList);

        mvc.perform(get(URL + "/{subcategoryId}/products", 1)
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldGetAllProductsBySubCategoryIdPaginationWithSort() throws Exception {
        given(productService.getAllProductsBySubcategoryId(1L, 0, 3, "price"))
                .willReturn(productDtoList);

        mvc.perform(get(URL + "/{subcategoryId}/products", 1)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldGetAmountProductsBySubcategoryId() throws Exception {
        given(productService.getQuantityOfProductsBySubcategoryId(anyLong())).willReturn(12L);

        mvc.perform(get(URL + "/{subcategoryId}/products/amount", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(12)))
                .andDo(print());
    }

    @Test
    void shouldSaveProduct() throws Exception {
        given(productService.save(1L, 1L, productDto))
                .willReturn(productDto);

        mvc.perform(post(URL + "/{subcategoryId}/producers/{producerId}/products", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.description", is("Description test")))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        given(productService.updateProduct(1L, 1L, 1L, productDto))
                .willReturn(productDto);

        mvc.perform(put(URL + "/{subcategoryId}/producers/{producerId}/products/{productId}", 1, 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.description", is("Description test")))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        mvc.perform(delete(URL + "/products/{productId}" , 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}