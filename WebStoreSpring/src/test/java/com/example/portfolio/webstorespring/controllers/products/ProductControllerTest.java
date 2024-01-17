package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
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

import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper.createProductRequest;
import static com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper.createProductResponse;
import static com.example.portfolio.webstorespring.buildhelpers.ProductWithProducerAndPromotionDTOBuilderHelper.createProductWithProducerAndPromotionDTO;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
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
    private static final String URI = "/api/v1";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        ProductResponse productResponse = createProductResponse();
        List<ProductResponse> productResponses = List.of(productResponse, productResponse, productResponse);

        given(productService.getAllProducts()).willReturn(productResponses);

        mvc.perform(get(URI + "/admin/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldGetProductById() throws Exception {
        ProductWithProducerAndPromotionDTO productWithProducerAndPromotionDTO = createProductWithProducerAndPromotionDTO();

        given(productService.getProductById(anyLong())).willReturn(productWithProducerAndPromotionDTO);

        mvc.perform(get(URI + "/products/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productWithProducerAndPromotionDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldSaveProduct() throws Exception {
        ProductResponse productResponse = createProductResponse();
        ProductRequest productRequest = createProductRequest();

        given(productService.saveProduct(anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(post(URI + "/admin/subcategories/{subcategoryId}/producers/{producerId}/products", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(productRequest.getName())))
                .andExpect(jsonPath("$.description", is(productRequest.getDescription())))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductResponse productResponse = createProductResponse();
        ProductRequest productRequest = createProductRequest();

        given(productService.updateProduct(anyLong(), anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(put(URI + "/admin/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}", 1, 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(productRequest.getName())))
                .andExpect(jsonPath("$.description", is(productRequest.getDescription())))
                .andExpect(jsonPath("$.price", is(20.0)))
                .andDo(print());
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        mvc.perform(delete(URI + "/admin/subcategories/products/{productId}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
