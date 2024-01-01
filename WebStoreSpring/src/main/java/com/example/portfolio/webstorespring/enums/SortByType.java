package com.example.portfolio.webstorespring.enums;

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

    public String getFieldName() {
        return fieldName;
    }
}

