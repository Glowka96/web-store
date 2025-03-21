package com.example.portfolio.webstorespring.controllers.products;


import com.example.portfolio.webstorespring.models.dtos.products.requests.CategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.CategoryResponse;
import com.example.portfolio.webstorespring.services.products.CategoryService;
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

import java.util.Arrays;

import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategoryRequest;
import static com.example.portfolio.webstorespring.buildhelpers.products.CategoryBuilderHelper.createCategoryResponse;
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
class CategoryControllerTest {
    @InjectMocks
    private CategoryController underTest;
    @Mock
    private CategoryService categoryService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private static final String URI = "/api/v1";

    @BeforeEach
    public void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllCategory() throws Exception {
        CategoryResponse categoryResponse = createCategoryResponse();
        given(categoryService.getAll()).willReturn(Arrays.asList(categoryResponse, categoryResponse));

        mvc.perform(get(URI + "/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldSaveCategory() throws Exception {
        CategoryResponse categoryResponse = createCategoryResponse();
        CategoryRequest categoryRequest = createCategoryRequest();

        given(categoryService.save(any(CategoryRequest.class))).willReturn(categoryResponse);

        mvc.perform(post(URI + "/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryResponse.name())))
                .andDo(print());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        CategoryResponse categoryResponse = createCategoryResponse();
        CategoryRequest categoryRequest = createCategoryRequest();

        given(categoryService.update(anyLong(), any(CategoryRequest.class))).willReturn(categoryResponse);

        mvc.perform(put(URI + "/admin/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryResponse.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryResponse.name())))
                .andDo(print());
    }

    @Test
    void shouldDeleteCategoryById() throws Exception {
        mvc.perform(delete(URI + "/admin/categories/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
