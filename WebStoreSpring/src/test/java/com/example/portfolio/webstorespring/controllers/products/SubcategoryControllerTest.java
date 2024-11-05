package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.services.products.SubcategoryService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper.createSubcategoryResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubcategoryControllerTest {

    @InjectMocks
    private SubcategoryController underTest;
    @Mock
    private SubcategoryService subcategoryService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllSubcategory() throws Exception {
        SubcategoryResponse subcategoryResponse = createSubcategoryResponse();

        given(subcategoryService.getAllSubcategory()).willReturn(List.of(subcategoryResponse, subcategoryResponse));

        mvc.perform(get(URI + "/admin/categories/subcategories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }


    @Test
    void shouldSaveSubcategory() throws Exception {
        SubcategoryResponse subcategoryResponse = createSubcategoryResponse();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest();

        given(subcategoryService.saveSubcategory(anyLong(), any(SubcategoryRequest.class))).willReturn(subcategoryResponse);

        mvc.perform(post(URI + "/admin/categories/{id}/subcategories", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subcategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(subcategoryResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is(subcategoryResponse.getName())))
                .andDo(print());
    }

    @Test
    void shouldUpdateSubcategory() throws Exception {
        SubcategoryResponse subcategoryResponse = createSubcategoryResponse();
        SubcategoryRequest subcategoryRequest = createSubcategoryRequest();

        given(subcategoryService.updateSubcategory(anyLong(), anyLong(), any(SubcategoryRequest.class))).willReturn(subcategoryResponse);

        mvc.perform(put(URI + "/admin/categories/{categoryId}/subcategories/{subcategoryId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subcategoryRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(subcategoryResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is(subcategoryResponse.getName())))
                .andDo(print());
    }

    @Test
    void shouldDeleteSubcategoryByName() throws Exception {
        mvc.perform(delete(URI + "/admin/categories/subcategories/{subcategoryId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
