package com.test.library.main.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BookDto (
    UUID id,
    String title,
    String author,
    String synopsis,
    boolean isAvailable,
    ZonedDateTime lastUnreturnedCheckoutAt,
    ZonedDateTime expectedReturnDate,
    CheckOutUserDto currentBorrower
) { }
