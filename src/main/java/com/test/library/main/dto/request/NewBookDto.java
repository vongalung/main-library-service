package com.test.library.main.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewBookDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        String synopsis
) { }
