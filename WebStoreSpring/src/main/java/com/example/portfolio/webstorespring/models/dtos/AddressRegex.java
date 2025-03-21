package com.example.portfolio.webstorespring.models.dtos;

public class AddressRegex {

    public static final String CITY = "^([A-Z]?[a-z]{2,30})$";
    public static final String POSTCODE = "^(\\d{2}-\\d{3})$";
    public static final String STREET =
            "^((ul\\.?\\s)?([A-Z]?[a-z]{2,20}(-[A-Z]?[a-z]{2,20})?)\\s(\\d{1,3})[a-z]?((/|\\sm.?\\s)\\d{1,3})?)$";

    private AddressRegex() {
    }
}
