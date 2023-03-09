package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
import com.example.porfolio.webstorespring.services.SubcategoryService;
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

import static org.hamcrest.CoreMatchers.is;
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
    private SubcategoryService subCategoryService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URL = "/api/v1/categories";
    private SubcategoryDto subCategoryDto;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        subCategoryDto = new SubcategoryDto();
        subCategoryDto.setId(1L);
        subCategoryDto.setName("Test");
    }

    @Test
    void shouldGetSubCategoryByName() throws Exception {
        // given
        given(subCategoryService.getSubcategoryDtoById(1L)).willReturn(subCategoryDto);

        // when
        // then
        mvc.perform(get(URL + "/subcategories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }


    @Test
    public void shouldSaveSubCategory() throws Exception {
        // given
        given(subCategoryService.save(1L, subCategoryDto)).willReturn(subCategoryDto);

        // when
        // then
        mvc.perform(post(URL + "/{id}/subcategories", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subCategoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldUpdateSubCategory() throws Exception {
        // given
        given(subCategoryService.update(1L, 1L, subCategoryDto)).willReturn(subCategoryDto);

        // when
        // then
        mvc.perform(put(URL + "/{categoryId}/subcategories/{subcategoryId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subCategoryDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldDeleteSubCategoryByName() throws Exception {
        mvc.perform(delete(URL + "/subcategories/{subcategoryId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}