package com.example.portfolio.webstorespring.buildhelpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateForTestBuilderHelper {

    public static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(
            2024,
            1,
            10,
            20,
            20,
            0,
            0,
            ZoneId.of("Europe/Warsaw")
    );

    public static final LocalDateTime LOCAL_DATE_TIME = ZONED_DATE_TIME.toLocalDateTime();
}
