package com.example.portfolio.webstorespring.controllers.products;


import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
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

import static com.example.portfolio.webstorespring.buildhelpers.CategoryBuilderHelper.createCategoryRequest;
import static com.example.portfolio.webstorespring.buildhelpers.CategoryBuilderHelper.createCategoryResponse;
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
    private final static String URI = "/api/v1";

    @BeforeEach
    public void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllCategory() throws Exception {
        // given
        CategoryResponse categoryResponse = createCategoryResponse();
        given(categoryService.getAllCategory()).willReturn(Arrays.asList(categoryResponse, categoryResponse));

        // when
        // then
        mvc.perform(get(URI + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        // given
        CategoryResponse categoryResponse = createCategoryResponse();
        given(categoryService.getCategoryById(anyLong())).willReturn(categoryResponse);

        // when
        // then
        mvc.perform(get(URI + "/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldSaveCategory() throws Exception {
        // given
        CategoryResponse categoryResponse = createCategoryResponse();
        CategoryRequest categoryRequest = createCategoryRequest();

        given(categoryService.saveCategory(any(CategoryRequest.class))).willReturn(categoryResponse);

        // when
        // then
        mvc.perform(post(URI + "/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        // given
        CategoryResponse categoryResponse = createCategoryResponse();
        CategoryRequest categoryRequest = createCategoryRequest();

        given(categoryService.updateCategory(anyLong(), any(CategoryRequest.class))).willReturn(categoryResponse);

        // when
        // then
        mvc.perform(put(URI + "/admin/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldDeleteCategoryById() throws Exception {
        mvc.perform(delete(URI + "/admin/categories/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
