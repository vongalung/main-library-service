package com.test.library.main.dto.response;

import java.time.ZonedDateTime;

public record CheckOutBookDto(
        String title,
        String author,
        ZonedDateTime lastUnreturnedCheckoutAt,
        ZonedDateTime expectedReturnDate
) {}
