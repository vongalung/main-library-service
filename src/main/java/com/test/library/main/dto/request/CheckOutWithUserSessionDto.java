package com.test.library.main.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CheckOutWithUserSessionDto(
    @NotNull UUID userId,
    LocalDate expectedReturnDate
) { }
