package com.example.portfolio.webstorespring.model.dto;

public class AddressRegex {

    public static final String CITY = "([A-Z]?[a-z]{2,30})";
    public static final String POSTCODE = "(\\d{2}-\\d{3})";
    public static final String STREET =
            "((ul\\.?\\s)?([A-Z]?[a-z]{3,}(-[A-Z]?[a-z]{3,})?)\\s(\\d{1,3})[a-z]?((/|\\sm.?\\s)\\d{1,3}))";
    public static final String COMPLETE_ADDRESS =
            CITY + ",\\s" + POSTCODE + ",\\s" + STREET;
}
