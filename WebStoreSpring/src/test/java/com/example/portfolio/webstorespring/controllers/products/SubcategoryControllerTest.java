package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.SubcategoryResponse;
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

import static org.hamcrest.CoreMatchers.is;
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
    private SubcategoryService subCategoryService;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private final static String URL = "/api/v1/categories";
    private SubcategoryResponse subcategoryResponse;

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();

        mapper = new ObjectMapper();

        subcategoryResponse = new SubcategoryResponse();
        subcategoryResponse.setId(1L);
        subcategoryResponse.setName("Test");
    }

    @Test
    void shouldGetSubCategoryByName() throws Exception {
        // given
        given(subCategoryService.getSubcategoryDtoById(anyLong())).willReturn(subcategoryResponse);

        // when
        // then
        mvc.perform(get(URL + "/subcategories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subcategoryResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }


    @Test
    void shouldSaveSubCategory() throws Exception {
        // given
        given(subCategoryService.save(anyLong(), any(SubcategoryRequest.class))).willReturn(subcategoryResponse);

        // when
        // then
        mvc.perform(post(URL + "/{id}/subcategories", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subcategoryResponse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andDo(print());
    }

    @Test
    void shouldUpdateSubCategory() throws Exception {
        // given
        given(subCategoryService.update(anyLong(), anyLong(), any(SubcategoryRequest.class))).willReturn(subcategoryResponse);

        // when
        // then
        mvc.perform(put(URL + "/{categoryId}/subcategories/{subcategoryId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subcategoryResponse)))
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
