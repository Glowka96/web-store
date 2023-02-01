package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import com.example.porfolio.webstorespring.services.CategoryService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    private MockMvc mvc;
    protected ObjectMapper mapper;
    private final static String URL = "/api/v1/categories";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllCategory() throws Exception {
        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Arrays.asList(
                                new CategoryDto(), new CategoryDto()))))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById() throws Exception {
        // given
        Long categoryId = 1L;

        given(categoryService.getCategoryDtoById(1L)).willReturn(any(CategoryDto.class));

        // then
        mvc.perform(get(URL + "/{id}", categoryId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSaveCategory() throws Exception {
        mvc.perform(post(URL)
                        .content(mapper.writeValueAsString(new CategoryDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCategory(){

    }
}