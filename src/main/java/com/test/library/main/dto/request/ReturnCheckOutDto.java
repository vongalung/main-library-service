package com.test.library.main.dto.request;

import com.test.library.main.model.ReturnStatus;
import jakarta.validation.constraints.NotNull;

public record ReturnCheckOutDto(
    @NotNull ReturnStatus returnStatus,
    String remarks
) { }
