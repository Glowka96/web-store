package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.services.products.ProductTypeService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductTypeBuilderHelper.createProductTypeResponse;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductTypeControllerTest {

    @Mock
    private ProductTypeService productTypeService;
    @InjectMocks
    private ProductTypeController underTest;
    private static final String URI = "/api/v1/admin/product-types";

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllProductType() throws Exception {
        ProductTypeResponse productTypeResponse = createProductTypeResponse();

        given(productTypeService.getAll()).willReturn(List.of(productTypeResponse, productTypeResponse));

        mvc.perform(get("/api/v1/product-types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());

    }

    @Test
    void saveProductType() throws Exception {
        ProductTypeRequest productTypeRequest = createProductTypeRequest();
        ProductTypeResponse productTypeResponse = createProductTypeResponse();

        given(productTypeService.save(any(ProductTypeRequest.class))).willReturn(productTypeResponse);

        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productTypeResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(productTypeResponse.name())))
                .andDo(print());
    }

    @Test
    void updateProductType() throws Exception {
        ProductTypeRequest productTypeRequest = createProductTypeRequest();
        ProductTypeResponse productTypeResponse = createProductTypeResponse();

        given(productTypeService.update(anyLong(), any(ProductTypeRequest.class)))
                .willReturn(productTypeResponse);

        mvc.perform(put(URI + "/{productTypeId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productTypeResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(productTypeResponse.name())))
                .andDo(print());
    }

    @Test
    void deleteProductTypeById() throws Exception {
        mvc.perform(delete(URI + "/{productTypeId}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());

    }
}
