package com.example.portfolio.webstorespring.converters;

import com.example.portfolio.webstorespring.enums.SortDirectionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SortDirectionTypeConverter implements Converter<String, SortDirectionType> {

    @Override
    public SortDirectionType convert(@NotNull String source) {
       return Arrays.stream(SortDirectionType.values())
               .filter(sortDirectionType -> sortDirectionType.name().equalsIgnoreCase(source))
               .findFirst()
               .orElseThrow(() -> new IllegalArgumentException("Invalid sort direction value: " + source));
    }
}
