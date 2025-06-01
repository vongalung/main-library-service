package com.test.library.main.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record LoginResponseDto(
        UUID userSession,
        ZonedDateTime expiresAt
) {}
