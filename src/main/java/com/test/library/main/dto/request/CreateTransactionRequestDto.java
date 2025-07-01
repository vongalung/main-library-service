package com.test.library.main.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateTransactionRequestDto(
        @NotEmpty List<@NotBlank String> transactionReasons
) {}
