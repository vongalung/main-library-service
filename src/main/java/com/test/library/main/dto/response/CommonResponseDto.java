package com.test.library.main.dto.response;

import java.time.ZonedDateTime;

public record CommonResponseDto(
        ZonedDateTime responseTime,
        String message
) {
    public static CommonResponseDto generateWithMessage(String message) {
        return new CommonResponseDto(ZonedDateTime.now(), message);
    }
}
