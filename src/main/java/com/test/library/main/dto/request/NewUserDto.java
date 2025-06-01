package com.test.library.main.dto.request;

import com.test.library.main.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUserDto(
        @NotBlank @Email String email,
        @NotBlank String fullName,
        UserRole userRole,
        @NotBlank @Size(min = 8, max = 8, message = "length must be 8")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must only contains alphanumeric")
        @Pattern(regexp = ".*[A-Z].*", message = "must have at least a capital")
        String password
) { }
