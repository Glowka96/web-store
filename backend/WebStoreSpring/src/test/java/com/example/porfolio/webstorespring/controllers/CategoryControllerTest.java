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
import org.springframework.web.bind.MethodArgumentNotValidException;

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
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test");
        categoryDto.setId(categoryId);

        // when
        when(categoryService.getCategoryDtoById(categoryId)).thenReturn(categoryDto);

        // then
        mvc.perform(get(URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.name", is("Test")));
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
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    void willThrowWhenSendPostForCategoryNameIsShortOrLong() throws Exception {
        // given
        categoryDto.setName("Er");

        // when
        // then
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDto))).andDo(print())
                .andExpect(status().isBadRequest());
        /*TODO:
            ** REPAIR THIS - NOT RETURN JSON

                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message",is("The name must between min 3 and max 20 letters")));*/    }

    @Test
    void updateCategory() {
        // given
        String name = "New name";
        CategoryDto categoryDto;

        // then
        // mvc.perform(put(URL + "/{name}"))
    }
}