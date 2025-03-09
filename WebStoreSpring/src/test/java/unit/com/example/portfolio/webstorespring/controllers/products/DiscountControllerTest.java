package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.DiscountRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountAdminResponse;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountUserResponse;
import com.example.portfolio.webstorespring.services.products.DiscountService;
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

import static com.example.portfolio.webstorespring.buildhelpers.products.DiscountBuilderHelper.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DiscountControllerTest {

    @Mock
    private DiscountService discountService;
    @InjectMocks
    private DiscountController underTest;

    private MockMvc mvc;
    private ObjectMapper mapper;

    private static final String URI = "/api/v1";

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(underTest).build();
        mapper = new ObjectMapper();
    }

    @Test
    void shouldGetDiscountByCode() throws Exception {
        String code = "example09";
        DiscountUserResponse discountUserResponse = createDiscountUserResponse();
        given(discountService.getByCode(anyString())).willReturn(discountUserResponse);

        mvc.perform(get(URI + "/discounts/{code}", code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountRate", is(0.1)))
                .andExpect(jsonPath("$.subcategoryIds", hasSize(2)))
                .andDo(print());
    }

    @Test
    void saveDiscount() throws Exception {
        DiscountRequest request = createDiscountRequestWithCode();
        DiscountAdminResponse response = createDiscountAdminResponse();

        given(discountService.save(request)).willReturn(response);

        mvc.perform(post(URI + "/admin/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(response.code())))
                .andExpect(jsonPath("$.discountRate", is(response.discountRate().doubleValue())))
                .andExpect(jsonPath("$.quantity", is(response.quantity().intValue())))
                .andExpect(jsonPath("$.subcategoryNames", hasItem(response.subcategoryNames().iterator().next())))
                .andDo(print());
    }

    @Test
    void deleteUserOrExpiredDiscount() throws Exception {
        mvc.perform(delete(URI + "/admin/discounts"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}