package com.test.library.main.dto.response;

public record UserDto(
        String email,
        String fullName,
        Boolean isLoggedIn,
        CheckOutBookDto activeCheckOut
) { }
