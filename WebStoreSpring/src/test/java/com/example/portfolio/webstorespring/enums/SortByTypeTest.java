package com.example.portfolio.webstorespring.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SortByTypeTest {

    @Test
    void shouldFindFieldNameOfSortByType_whenValidSortByTypeNameIsGiven() {
        assertThat(SortByType.findFieldNameOfSortByType("name")).isEqualTo("name");
        assertThat(SortByType.findFieldNameOfSortByType("price")).isEqualTo("price");
        assertThat(SortByType.findFieldNameOfSortByType("type")).isEqualTo("type");
        assertThat(SortByType.findFieldNameOfSortByType("date")).isEqualTo("dateOfCreation");
    }

    @Test
    void shouldFindFieldNameOfSortByType_whenValidSortByTypeNameIsDifferentCase() {
        assertThat(SortByType.findFieldNameOfSortByType("NAME")).isEqualTo("name");
        assertThat(SortByType.findFieldNameOfSortByType("PrIcE")).isEqualTo("price");
        assertThat(SortByType.findFieldNameOfSortByType("TyPe")).isEqualTo("type");
        assertThat(SortByType.findFieldNameOfSortByType("dAtE")).isEqualTo("dateOfCreation");
    }

    @Test
    void shouldThrowException_whenInvalidSortByTypeNameIsGiven() {
        assertThatThrownBy(() -> SortByType.findFieldNameOfSortByType("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid sort type value: invalid");
    }
}
