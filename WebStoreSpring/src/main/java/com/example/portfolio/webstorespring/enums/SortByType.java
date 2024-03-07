package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum SortByType {
    ID("id"),
    NAME("name"),
    PRICE("price"),
    TYPE("type"),
    DATE("dateOfCreation");

    private final String fieldName;

    SortByType(String fieldName) {
        this.fieldName = fieldName;
    }
}

