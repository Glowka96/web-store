package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductQualityRequest;
import com.example.portfolio.webstorespring.models.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.services.products.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProductRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProductResponse;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithProducerAndPromotionDTOBuilderHelper.createProductWithProducerAndPromotionDTO;
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
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        ProductResponse productResponse = createProductResponse();
        List<ProductResponse> productResponses = List.of(productResponse, productResponse, productResponse);

        given(productService.getAll()).willReturn(productResponses);

        mvc.perform(get(URI + "/admin/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void shouldGetProductById() throws Exception {
        ProductWithProducerAndPromotionDTO productWithProducerAndPromotionDTO = createProductWithProducerAndPromotionDTO();

        given(productService.getById(anyLong())).willReturn(productWithProducerAndPromotionDTO);

        mvc.perform(get(URI + "/products/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productWithProducerAndPromotionDTO.id().intValue())))
                .andExpect(jsonPath("$.name", is(productWithProducerAndPromotionDTO.name())))
                .andExpect(jsonPath("$.imageUrl", is(productWithProducerAndPromotionDTO.imageUrl())))
                .andExpect(jsonPath("$.quantity", is(productWithProducerAndPromotionDTO.quantity().intValue())))
                .andExpect(jsonPath("$.productTypeName", is(productWithProducerAndPromotionDTO.productTypeName())))
                .andExpect(jsonPath("$.price", is(productWithProducerAndPromotionDTO.price().doubleValue())))
                .andExpect(jsonPath("$.promotionPrice", is(productWithProducerAndPromotionDTO.promotionPrice().doubleValue())))
                .andExpect(jsonPath("$.lowestPrice", is(productWithProducerAndPromotionDTO.lowestPrice().doubleValue())))
                .andExpect(jsonPath("$.endDate", is(productWithProducerAndPromotionDTO.endDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
                .andExpect(jsonPath("$.description", is(productWithProducerAndPromotionDTO.description())))
                .andExpect(jsonPath("$.producerName", is(productWithProducerAndPromotionDTO.producerName())))
                .andDo(print());
    }

    @Test
    void shouldSaveProduct() throws Exception {
        ProductResponse productResponse = createProductResponse();
        ProductRequest productRequest = createProductRequest();

        given(productService.save(anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(post(URI + "/admin/subcategories/{subcategoryId}/producers/{producerId}/products", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(productResponse.name())))
                .andExpect(jsonPath("$.description", is(productResponse.description())))
                .andExpect(jsonPath("$.price", is(productResponse.price().doubleValue())))
                .andDo(print());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductResponse productResponse = createProductResponse();
        ProductRequest productRequest = createProductRequest();

        given(productService.update(anyLong(), anyLong(), anyLong(), any(ProductRequest.class)))
                .willReturn(productResponse);

        mvc.perform(put(URI + "/admin/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}", 1, 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest))
                        .header("Authorization", "Bearer {JWT_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(productResponse.name())))
                .andExpect(jsonPath("$.description", is(productResponse.description())))
                .andExpect(jsonPath("$.price", is(productResponse.price().doubleValue())))
                .andDo(print());
    }

    @Test
    void shouldUpdateProductQuantity() throws Exception {
        ProductQualityRequest request = new ProductQualityRequest(1L, 10L);
        ResponseMessageDTO response = new ResponseMessageDTO("The product quantity was updated successfully.");

        given(productService.updateQuality(any(ProductQualityRequest.class))).willReturn(response);

        mvc.perform(patch(URI + "/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(response.message())))
                .andDo(print());
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        mvc.perform(delete(URI + "/admin/products/{productId}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
