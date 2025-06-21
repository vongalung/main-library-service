package com.test.library.main.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BookUnitDto(
    UUID id,
    boolean isAvailable,
    ZonedDateTime lastUnreturnedCheckoutAt,
    ZonedDateTime expectedReturnDate,
    CheckOutUserDto currentBorrower
) { }
