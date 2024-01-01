package com.example.portfolio.webstorespring.converters;

import com.example.portfolio.webstorespring.enums.SortByType;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SortByTypeConverter implements Converter<String, SortByType> {

    @Override
    public SortByType convert(@NotNull String source) {
        return Arrays.stream(SortByType.values())
                .filter(sortByType -> sortByType.name().equalsIgnoreCase(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid sort type value: " + source));
    }
}
