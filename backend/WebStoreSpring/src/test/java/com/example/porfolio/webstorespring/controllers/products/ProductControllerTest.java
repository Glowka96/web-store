package com.example.porfolio.webstorespring.controllers.products;

import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.porfolio.webstorespring.model.entity.products.ProductType;
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
import static org.mockito.ArgumentMatchers.*;
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
    private ProductResponse productResponse;
    private List<ProductResponse> productResponses;


    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Test");
        productResponse.setPrice(20.0);
        productResponse.setDescription("Description test");
        productResponse.setImageUrl("https://i.imgur.com/a23SANX.png");
        productResponse.setType(ProductType.PUZZLE);

        productResponses = new ArrayList<>(Arrays.asList(productResponse, new ProductResponse(), new ProductResponse()));
    }

    @Test
    void shouldGetAllProductsBySubCategoryIdPaginationNoSort() throws Exception {
        given(productService.getAllProductsBySubcategoryId(anyLong(), anyInt(), anyInt()))
                .willReturn(productResponses);

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
        given(productService.getAllProductsBySubcategoryId(anyLong(), anyInt(), anyInt(), anyString()))
                .willReturn(productResponses);

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
        given(productService.save(anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(post(URL + "/{subcategoryId}/producers/{producerId}/products", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productResponse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.description", is("Description test")))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        given(productService.updateProduct(anyLong(), anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(put(URL + "/{subcategoryId}/producers/{producerId}/products/{productId}", 1, 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productResponse)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.description", is("Description test")))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        mvc.perform(delete(URL + "/products/{productId}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}