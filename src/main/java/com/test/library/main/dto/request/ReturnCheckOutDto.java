package com.test.library.main.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReturnCheckOutDto(
    @NotNull UUID returnStatusId,
    String remarks
) { }
