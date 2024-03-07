package com.example.portfolio.webstorespring.converters;

import com.example.portfolio.webstorespring.enums.SortDirectionType;
import org.springframework.stereotype.Component;

@Component
public final class SortDirectionTypeConverter extends AbstractConverter<SortDirectionType> {

    @Override
    protected Class<SortDirectionType> getEnumType() {
        return SortDirectionType.class;
    }

    @Override
    protected String getErrorMessage() {
        return "Invalid sort direction value: ";
    }
}
