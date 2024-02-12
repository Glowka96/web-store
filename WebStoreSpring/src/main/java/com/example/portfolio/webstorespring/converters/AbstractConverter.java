package com.example.portfolio.webstorespring.converters;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

abstract class AbstractConverter<E extends Enum<E>> implements Converter<String, E> {
    @Override
    public E convert(@NotNull String source) {
        return Arrays.stream(getEnumType().getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage() + source));
    }

    protected abstract Class<E> getEnumType();

    protected abstract String getErrorMessage();
}
