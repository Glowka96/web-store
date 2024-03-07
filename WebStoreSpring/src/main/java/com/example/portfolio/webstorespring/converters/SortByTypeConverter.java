package com.example.portfolio.webstorespring.converters;

import com.example.portfolio.webstorespring.enums.SortByType;
import org.springframework.stereotype.Component;

@Component
public final class SortByTypeConverter extends AbstractConverter<SortByType> {

    @Override
    protected Class<SortByType> getEnumType() {
        return SortByType.class;
    }

    @Override
    protected String getErrorMessage() {
        return "Invalid sort type value: " ;
    }
}
