package com.test.library.main.common;

import static java.time.LocalTime.MIDNIGHT;
import static java.time.ZoneId.systemDefault;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateTimeUtils {
    public static ZonedDateTime convertToZonedAtLateMidnight(LocalDate date) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.of(date.plusDays(1), MIDNIGHT, systemDefault());
    }

    public static ZonedDateTime adjustToLateMidnight(ZonedDateTime origin) {
        if (origin == null) {
            return null;
        }
        return origin.toLocalDate().plusDays(1).atStartOfDay(origin.getZone());
    }
}
