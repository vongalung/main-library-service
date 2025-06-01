package com.test.library.main.dto.response;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class CommonResponseDto {
    private ZonedDateTime responseTime = ZonedDateTime.now();
    private String message;

    public static CommonResponseDto generateWithMessage(String message) {
        CommonResponseDto response = new CommonResponseDto();
        response.setMessage(message);
        return response;
    }
}
