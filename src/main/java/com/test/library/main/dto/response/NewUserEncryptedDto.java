package com.test.library.main.dto.response;

public record NewUserEncryptedDto(
        String email,
        String fullName,
        byte[] encryptedPassword
) { }
