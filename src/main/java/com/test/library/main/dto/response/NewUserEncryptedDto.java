package com.test.library.main.dto.response;

import com.test.library.main.model.UserRole;

public record NewUserEncryptedDto(
        String email,
        String fullName,
        UserRole userRole,
        byte[] encryptedPassword
) { }
