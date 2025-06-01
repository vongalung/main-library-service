package com.test.library.main.dto.request;

import com.test.library.main.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewUserDto(
        @NotBlank @Email String email,
        @NotBlank String fullName,
        UserRole userRole,
        @NotBlank @Max(8)
        @Pattern(regexp = "^([A-Z][a-zA-Z0-9]+|[a-zA-Z0-9]+[A-Z][a-zA-Z0-9]+|[a-zA-Z0-9]+[A-Z])$")
        String password
) { }
