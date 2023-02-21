package com.example.porfolio.webstorespring.controllers;


import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryService categoryService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URL = "/api/v1/categories";
    private CategoryDto categoryDto;

    @BeforeEach
    public void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        mapper = new ObjectMapper();
        categoryDto = new CategoryDto();
        categoryDto.setName("Test");
        categoryDto.setId(1L);
    }

    @Test
    void shouldGetAllCategory() throws Exception {
        // given
        List<CategoryDto> categoryDtoList = Arrays.asList(
                new CategoryDto(), new CategoryDto());

        // when
        when(categoryService.getAllCategoryDto()).thenReturn(categoryDtoList);

        // then
        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void shouldGetCategoryById() throws Exception {
        // given
        Long categoryId = 1L;

        // when
        when(categoryService.getCategoryDtoById(categoryId)).thenReturn(categoryDto);

        // then
        mvc.perform(get(URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldSaveCategory() throws Exception {
        // given
        // when
        when(categoryService.save(any(CategoryDto.class))).thenReturn(categoryDto);
        // then
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryDto newCategory = new CategoryDto();
        newCategory.setName("New name");

        CategoryDto exceptedCategoryDto = new CategoryDto();
        exceptedCategoryDto.setName("New name");
        exceptedCategoryDto.setId(1L);
        // when
        when(categoryService.update(categoryId, newCategory)).thenReturn(exceptedCategoryDto);

        // then
        mvc.perform(put(URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCategory)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name", is("New name")))
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(print());
    }

    @Test
    void shouldDeleteCategoryById() throws Exception {
        mvc.perform(delete(URL + "/{id}", 1L))
                .andExpect(status().isAccepted());
    }
}