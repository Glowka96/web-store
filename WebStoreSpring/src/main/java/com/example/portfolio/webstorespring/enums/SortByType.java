package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SortByType {

    NAME("name"),
    PRICE("price"),
    TYPE("type"),
    DATE("createdAt");

    private final String fieldName;
    private static final Map<String, String> BY_FIELD_NAME =
            Stream.of(values()).collect(Collectors.toMap(e -> e.name().toLowerCase(), SortByType::getFieldName));

    SortByType(String fieldName) {
        this.fieldName = fieldName;
    }

    public static String findFieldNameOfSortByType(String sortByTypeName) {
        String fieldName = BY_FIELD_NAME.get(sortByTypeName.toLowerCase());
        if(fieldName == null) {
            throw new IllegalArgumentException("Invalid sort type value: " + sortByTypeName);
        }
        return fieldName;
    }
}

